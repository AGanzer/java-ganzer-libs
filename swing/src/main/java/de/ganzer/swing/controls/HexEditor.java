package de.ganzer.swing.controls;

import javax.swing.JSplitPane;

public class HexEditor extends JSplitPane {
}
/*
- Der Hex-Editor besteht aus zwei Ansichten, die in einer `JSplitPane` untergebracht sind (`HexEditor` ist davon bereits abgeleitet). Die linke Ansicht zeigt den Hex-Code der Datei an, während die rechte Ansicht den Text der Datei anzeigt.
- In der Text-Ansicht wird der Text der Datei angezeigt. Nicht darstellbare Zeichen und Leerzeichen werden durch kleine Punkte markiert. Steuerzeichen (Linefeed, Tabs usw.) werden nicht ausgeführt, sondern durch ein entsprechendes ASCII-Zeichen dargestellt.
- In der Hex-Ansicht wird das jeweilige Zeichen als Unicode-Code (vier Hex-Ziffern) dargestellt, getrennt durch jeweils ein Leerzeichen.
- Wird in der Hex-Ansicht ein Zeichen gelöscht (mit Backspace- oder Delete-Taste), werden alle vier Hex-Ziffern gelöscht und entsprechend das Zeichen in der Text-Ansicht. Wird innerhalb der Hex-Ansicht ein Zeichen eingegeben, überschreibt es immer die Hex-Ziffer an der Caret-Position.
- Wird innerhalb der Hex-Ansicht zwischen zwei Hex-Codes (auf dem trennenden Leerzeichen) entweder die Taste Insert oder die Leertaste gedrückt, werden an der Position vier neue Null-Ziffern (0000) eingefügt. Andere Zeichen werden an dieser Position nicht erkannt (außer das Caret steht hinter dem Leerzeichen vor der folgenden Ziffer, dann tritt der folgende Punkt in Kraft).
 -In der Hex-Ansicht werden an der Position einer Hex-Ziffer nur die Tasten erkannt, die eine Hex-Ziffer repräsentieren ('0'-'F'), Delete und Backspace entfernen alle vier Ziffern (das gesamte Zeichen). Steht das Caret vor der ersten der vier ziffern (also hinter dem Leerzeichen), werden auch Insert- und Leertaste gemäß der Beschreibung im vorherigen Punkt erkannt.
- Wird in der Text-Ansicht ein Zeichen gelöscht, eingefügt oder geändert, wird die Hex-Ansicht entsprechend aktualisiert. Die Text-Ansicht kann "normal" editiert werden. Die Hex-Ansicht aktualisiert sich automatisch. Erfolgt in der Hex-Ansicht für das in der Text-Ansicht eingegebene Zeichen ein Umbruch in die nächste Zeile, geschieht das auch in der Text-Ansicht (das Caret wandert mit).
- Beide Ansichten verwenden einen Monospace-Font.
- Die Hex-Ansicht kann nur vertikal gescrollt werden. Es erfolgt ein automatischer Zeilenumbruch an der Grenze zwischen zwei Hex-Codes (an dem Leerzeichen).
- Es werden in der Text-Ansicht in einer Zeile nur so viele Zeichen angezeigt, wie in derselben Zeile in der Hex-Ansicht zu sehen sind. Diese Ansicht kann vertikal und horizontal gescrollt werden.
- Wird eine Ansicht vertikal gescrollt, scrollt die andere Ansicht Zeilen-synchron mit.
- Das Zeichen, an dem sich das Caret befindet, wird farblich hervorgehoben, und zwar in beiden Ansichten. Die Vorder- und Hintergrundfarbe können jeweils über Properties innerhalb der Klasse `HexEditor` eingestellt werden. Wird eine solche Property gesetzt, erfolgt sofort eine Aktualisierung der Ansichten. Die Standardfarben sind die Invert-Farben der Textbearbeitungs-Komponenten für Vorder- und Hintergrundfarbe. Diese Properties werden automatisch aktualisiert, wenn die Vorder- und Hintergrundfarbe für die Editoren geändert wird. Die Änderung all dieser Farben erfolgt in der Klasse `HexEditor` für beide enthaltenen Ansichten.
- In `HexEditor` steht für den verwendeten Font in den beiden Ansichten ebenfalls ein Getter und Setter bereit. Beide Ansichten verwenden immer denselben Font und dieselben Farben.
- Sollte es sich als ungünstig erweisen, dass `HexEditor` von `JSplitPane` ableitet, dann soll das ggf. geändert werden.
- Unit tests sollen für diese Komponente in dem Rahmen erstellt werden, der für eine Swing-Komponente sinnvoll ist.
 */
