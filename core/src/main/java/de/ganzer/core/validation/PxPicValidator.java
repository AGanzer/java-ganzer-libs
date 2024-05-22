package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;

/**
 * The PxPicValidator class defines a validator that validates text by using a
 * picture mask that complies to Borland's Paradox database pictures.
 * <p>
 * The mask uses the following characters:
 * <p>
 * Special chars:
 * <ul>
 *     <li>{@literal # -> Accept a decimal digit only.}
 *     <li>{@literal ? -> Accept a letter only (case independent).}
 *     <li>{@literal @ -> Accept every character.}
 *     <li>{@literal & -> Accept a letter only (force uppercase).}
 *     <li>{@literal ! -> Accept every character, force uppercase letters.}
 * </ul>
 * <p>
 * 	Comparison:
 * <ul>
 *     <li>{@literal ; -> Treat next char as is.}
 *     <li>{@literal * -> Repetition counter. May be followed by a decimal number.
 *     A number greater than 0 is required if a group or an option follows.}
 *     <li>{@literal [] -> Optional.}
 *     <li>{@literal {} -> group-operator.}
 *     <li>{@literal , -> Alternativ set.}
 * </ul>
 * <p>
 * 	All others: Treated as they are.
 * <p>
 * 	Examples:
 * <ul>
 *     <li>{@literal {White,Gr{ay,een},B{l{ack,ue},rown},Red} -> One of the given colors.}
 *     <li>{@literal ##/##/##[##] -> Date with or without century.}
 *     <li>{@literal &*? -> Letters only, first character uppercased.}
 *     <li>{@literal [(*3#*2[#]) ]*3#-*4# -> Telephone no. with opt. 3-5 digits area code.}
 * </ul>
 */
public class PxPicValidator extends Validator {
    private enum Status {
        COMPLETE,
        INCOMPLETE,
        EMPTY,
        ERROR,
        SYNTAX,
        AMBIGUOUS,
        INCOMPLETE_NO_FILL
    }

    private static class StateMachine {
        private final String picture;
        private final StringBuilder input;
        private int idxPic;
        private int idxInp;

        public StateMachine(String picture, StringBuilder input) {
            this.picture = picture;
            this.input = input;
        }

        public Status start(boolean fill) {
            if (input.length() == 0)
                return Status.EMPTY;

            idxInp = 0;
            idxPic = 0;

            Status result = process(picture.length());

            if (result != Status.ERROR && result != Status.SYNTAX && idxInp < input.length())
                result = Status.ERROR;

            // If the result is incomplete and filling is requested, then copy
            // literal characters from the picture over to the text:
            //
            if (result == Status.INCOMPLETE && fill) {
                var reprocess = false;
                var specchars = "#?&!@*{}[],";

                while (idxPic < picture.length() && specchars.indexOf(picture.charAt(idxPic)) < 0) {
                    if (picture.charAt(idxPic) == ';')
                        ++idxPic;

                    input.insert(idxInp++, picture.charAt(idxPic++));

                    reprocess = true;
                }

                if (reprocess) {
                    idxInp = 0;
                    idxPic = 0;
                    result = process(picture.length());
                }
            }

            if (result == Status.AMBIGUOUS)
                return Status.COMPLETE;

            if (result == Status.INCOMPLETE_NO_FILL)
                return Status.INCOMPLETE;

            return result;
        }

        private Status process(int term) {
            Status result;
            int incompleteJ = 0;
            int incompleteI = 0;
            boolean incomplete = false;
            int oldI = idxPic;
            int oldJ = idxInp;

            do {
                result = scan(term);

                // Only accept completes if they make it farther
                // in the input stream from the last incomplete:
                //
                if ((result == Status.COMPLETE || result == Status.AMBIGUOUS) && incomplete && idxInp < incompleteJ) {
                    result = Status.INCOMPLETE;
                    idxInp = incompleteJ;
                }

                if (result == Status.ERROR || result == Status.INCOMPLETE) {
                    if (!incomplete && result == Status.INCOMPLETE) {
                        incomplete = true;
                        incompleteI = idxPic;
                        incompleteJ = idxInp;
                    }

                    idxPic = oldI;
                    idxInp = oldJ;

                    if (!skipToComma(term)) {
                        if (incomplete) {
                            idxPic = incompleteI;
                            idxInp = incompleteJ;

                            return Status.INCOMPLETE;
                        }

                        return result;
                    }

                    oldI = idxPic;
                }
            }
            while (result == Status.ERROR || result == Status.INCOMPLETE);

            return (result == Status.COMPLETE && incomplete) ? Status.AMBIGUOUS : result;
        }

        private Status scan(int term) {
            char ch;
            Status result = Status.EMPTY;
            int len = input.length();

            while (idxPic != term && picture.charAt(idxPic) != ',') {
                if (idxInp >= len)
                    return checkComplete(term, result);

                ch = input.charAt(idxInp);

                switch (picture.charAt(idxPic)) {
                    case '#':
                        if (!Character.isDigit(ch))
                            return Status.ERROR;

                        ++idxInp;
                        ++idxPic;

                        break;

                    case '?':
                        if (!Character.isLetter(ch))
                            return Status.ERROR;

                        ++idxInp;
                        ++idxPic;

                        break;

                    case '&':
                        if (!Character.isLetter(ch))
                            return Status.ERROR;

                        input.setCharAt(idxInp++, Character.toUpperCase(ch));
                        ++idxPic;

                        break;

                    case '!': {
                        input.setCharAt(idxInp++, Character.toUpperCase(ch));
                        ++idxPic;

                        break;
                    }

                    case '@': {
                        ++idxInp;
                        ++idxPic;

                        break;
                    }

                    case '*':
                        result = iteration(term);

                        if (!isComplete(result))
                            return result;

                        if (result == Status.ERROR)
                            result = Status.AMBIGUOUS;

                        break;

                    case '{':
                        result = group(term);

                        if (!isComplete(result))
                            return result;

                        break;

                    case '[':
                        result = group(term);

                        if (isIncomplete(result))
                            return result;

                        if (result == Status.ERROR)
                            result = Status.AMBIGUOUS;

                        break;

                    default: {
                        if (picture.charAt(idxPic) == ';')
                            ++idxPic;

                        if (Character.toUpperCase(picture.charAt(idxPic)) != Character.toUpperCase(ch)) {
                            if (ch != ' ')
                                return Status.ERROR;
                        }

                        input.setCharAt(idxInp++, picture.charAt(idxPic++));
                    }
                }

                if (result == Status.AMBIGUOUS)
                    result = Status.INCOMPLETE_NO_FILL;
                else
                    result = Status.INCOMPLETE;
            }

            return (result == Status.INCOMPLETE_NO_FILL) ? Status.AMBIGUOUS : Status.COMPLETE;
        }

        private Status checkComplete(int term, Status result) {
            int j = idxPic;

            if (isIncomplete(result)) {
                // Skip optional pieces:
                //
                for (; ; ) {
                    if (picture.charAt(j) == '[')
                        j = toGroupEnd(term, j);
                    else if (picture.charAt(j) == '*') {
                        if (j < term && !Character.isDigit(picture.charAt(j + 1))) {
                            ++j;
                            j = toGroupEnd(term, j);
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }

                    if (j == term)
                        return Status.AMBIGUOUS; // End of the string, don't know if complete!
                }
            }

            return result;
        }

        private int toGroupEnd(int term, int pos) {
            int brk_level = 0;
            int brc_level = 0;
            int newPos = pos;

            do {
                if (newPos == term)
                    return newPos;

                switch (picture.charAt(newPos)) {
                    case '[':
                        ++brk_level;
                        break;

                    case ']':
                        --brk_level;
                        break;

                    case '{':
                        ++brc_level;
                        break;

                    case '}':
                        --brc_level;
                        break;

                    case ';':
                        ++newPos;
                        break;

                    case '*':
                        ++newPos;

                        while (Character.isDigit(picture.charAt(newPos)))
                            ++idxPic;

                        newPos = toGroupEnd(term, newPos);

                        continue;
                }

                ++newPos;
            }
            while (brk_level > 0 || brc_level > 0);

            return newPos;
        }

        private boolean isComplete(Status result) {
            return result == Status.COMPLETE || result == Status.AMBIGUOUS;
        }

        private boolean isIncomplete(Status result) {
            return result == Status.INCOMPLETE || result == Status.INCOMPLETE_NO_FILL;
        }

        private boolean skipToComma(int term) {
            while (true) {
                idxPic = toGroupEnd(term, idxPic);

                if (idxPic == term)
                    return false;

                if (picture.charAt(idxPic) == ',')
                    return ++idxPic < term;
            }
        }

        private Status group(int term) {
            int grpterm = calcTerm(term, idxPic);

            ++idxPic;

            Status rslt = process(grpterm - 1);

            if (!isIncomplete(rslt))
                idxPic = grpterm;

            return rslt;
        }

        private int calcTerm(int term, int pos) {
            return toGroupEnd(term, pos);
        }

        private Status iteration(int term) {
            ++idxPic;  // Skip '*'.

            Status rslt = Status.ERROR;
            int itr = 0;

            // Retrieve number:
            //
            while (idxPic < term && Character.isDigit(picture.charAt(idxPic))) {
                itr = itr * 10 + digitValue(picture.charAt(idxPic));
                ++idxPic;
            }

            if (idxPic == term)
                return Status.SYNTAX;

            int k = idxPic;
            int t = calcTerm(term, idxPic);

            // If itr is 0 allow any number, otherwise enforce the number:
            //
            if (itr != 0) {
                for (int m = 0; m < itr; ++m) {
                    idxPic = k;
                    rslt = process(t);

                    if (!isComplete(rslt)) {
                        // Empty means incomplete since all are required:
                        //
                        if (rslt == Status.EMPTY)
                            rslt = Status.INCOMPLETE;

                        return rslt;
                    }
                }
            } else {
                do {
                    idxPic = k;
                    rslt = process(t);
                }
                while (isComplete(rslt));

                if (rslt == Status.EMPTY || rslt == Status.ERROR) {
                    ++idxPic;
                    rslt = Status.AMBIGUOUS;
                }
            }

            idxPic = t;

            return rslt;
        }

        private int digitValue(char ch) {
            return (int)ch - (int)'0';
        }
    }

    private String picture = "";

    /**
     * Creates a new instance of the validator where every input is valid.
     * <p>
     * This sets the {@link #getOptions options} to
     * {@code {@link ValidatorOptions#NEEDS_INPUT} | {@link ValidatorOptions#AUTO_FILL}}.
     */
    public PxPicValidator() {
        super(ValidatorOptions.NEEDS_INPUT | ValidatorOptions.AUTO_FILL);
    }

    /**
     * Creates a new instance of the validator where every input is valid.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     */
    public PxPicValidator(int options) {
        super(options);
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getOptions options} to
     * {@code {@link ValidatorOptions#NEEDS_INPUT} | {@link ValidatorOptions#AUTO_FILL}}.
     * <p>
     * If the picture's syntax is invalid, an {@code IllegalArgumentException}
     * is thrown. To avoid this, the picture's syntax can be validated by
     * {@link #checkSyntax(String)}.
     *
     * @param picture The picture to set. An empty string makes every input valid.
     * @throws NullPointerException picture is {@code null}.
     * @throws IllegalArgumentException picture contains an invalid mask.
     */
    public PxPicValidator(String picture) {
        super(ValidatorOptions.NEEDS_INPUT | ValidatorOptions.AUTO_FILL);
        setPicture(picture);
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * If the picture's syntax is invalid, an {@code IllegalArgumentException}
     * is thrown. To avoid this, the picture's syntax can be validated by
     * {@link #checkSyntax(String)}.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     * @param picture The picture to set. An empty string makes every input valid.
     * @throws NullPointerException picture is {@code null}.
     * @throws IllegalArgumentException picture contains an invalid mask.
     */
    public PxPicValidator(int options, String picture) {
        super(options);
        setPicture(picture);
    }

    /**
     * Gets the picture that is used for validation.
     *
     * @return The used picture or an empty string if every input is valid.
     */
    public String getPicture() {
        return picture;
    }

    /**
     * Sets the picture to use for validation.
     * <p>
     * If the picture's syntax is invalid, an {@code IllegalArgumentException}
     * is thrown. To avoid this, the picture's syntax can be validated by
     * {@link #checkSyntax(String)}.
     *
     * @param picture The picture to set. An empty string makes every input valid.
     * @throws NullPointerException picture is {@code null}.
     * @throws IllegalArgumentException picture contains an invalid mask.
     */
    public void setPicture(String picture) {
        if (picture == null )
            throw new NullPointerException("picture");

        if (!checkSyntax(picture))
            throw new IllegalArgumentException("picture");

        this.picture = picture;
    }

    /**
     * Checks whether the given picture is syntactically valid.
     *
     * @param picture The picture to check.
     * @return {@code true} if picture is valid; otherwise, {@code false} is
     * returned.
     */
    public boolean checkSyntax(String picture) {
        if (picture.length() == 0)
            return true;

        char lastChar = picture.charAt(picture.length() - 1);
        char prevLastChar = picture.length() > 1 ? picture.charAt(picture.length() - 2) : '\0';

        if (lastChar == ';' && (picture.length() < 2 || prevLastChar != ';'))
            return false;

        if (lastChar == '*' && (picture.length() < 2 || prevLastChar != ';'))
            return false;

        var brk_level = 0;
        var brc_level = 0;
        var groups = "[]{}";

        for (int p = 0; p < picture.length(); ++p) {
            switch (picture.charAt(p)) {
                case '*':
                    if ((p + 1) < picture.length() && groups.indexOf(picture.charAt(p + 1)) >= 0)
                        return false;

                    break;

                case '[':
                    ++brk_level;
                    break;

                case ']':
                    --brk_level;
                    break;

                case '{':
                    ++brc_level;
                    break;

                case '}':
                    --brc_level;
                    break;

                case ';':
                    ++p;
            }
        }

        return brk_level == 0 && brc_level == 0;
    }

    /**
     * This implementation calls {@link Validator#doInputValidation} and checks
     * whether the input does match the picture.
     *
     * @param text     The text to validate. This is never {@code null}. The text
     *                 may be modified if autoFill is {@code true}.
     * @param autoFill Indicates whether the text is allowed to be modified.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    @Override
    protected boolean doInputValidation(StringBuilder text, boolean autoFill) {
        if (!super.doInputValidation(text, autoFill))
            return false;

        if (picture.length() == 0 || text.length() == 0)
            return true;

        switch (new StateMachine(picture, text).start(autoFill)) {
            case COMPLETE:
            case AMBIGUOUS:
            case INCOMPLETE:
            case INCOMPLETE_NO_FILL:
                return true;

            default:
                return false;
        }
    }

    /**
     * This implementation calls the {@link Validator#doInputValidation} and checks
     * whether the input does match the picture.
     *
     * @param text The text to validate. This is never {@code null}.
     * @param er   A reference to a possible exception. If this method returns
     *             {@code false}, the encapsulated exception is set to an
     *             instance of {@link ValidatorException}. This must not be
     *             {@code null}.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    @Override
    protected boolean doValidate(String text, ValidatorExceptionRef er) {
        if (!super.doValidate(text, er))
            return false;

        if (picture.length() == 0 || text.length() == 0)
            return true;

        switch (new StateMachine(picture, new StringBuilder(text)).start(false)) {
            case COMPLETE:
            case EMPTY:
                return true;

            case SYNTAX:
                er.setException(new ValidatorException(getErrorMessage() != null
                        ? getErrorMessage()
                        : String.format(CoreMessages.get("picSyntaxError"), picture)));

                return false;

            default:
                er.setException(new ValidatorException(getErrorMessage() != null
                        ? getErrorMessage()
                        : String.format(CoreMessages.get("inputDoesNotConfirmPic"), picture)));

                return false;
        }
    }
}
