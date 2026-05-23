package de.ganzer.core.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A singleton class that provides services.
 * <p>
 * A service is any implementation of a service interface or class. Wich service
 * is got by {@link #get(Class)} depends on the previous registration. For
 * instance:
 * <p>
 * <pre>{@code
 * public static void main(String[] args) {
 *     ServiceProvider.register(LoginService.class, new MyLoginServiceImpl());
 *
 *     if (doLogin())
 *         run();
 * }
 *
 * private static boolean doLogin() {
 *     var service = ServiceProvider.get(LoginService.class);
 *     var loginData = new LoginData();
 *
 *     if (!service.login(loginData))
 *         return false;
 *
 *     finishLogin(loginData);
 *     return true;
 * }
 * }</pre>
 * <p>
 * The usage of services enables the application to change behaviors by changing
 * the implementation of the required service without changing any code that uses
 * the service.
 *
 * @since 5.6.0
 */
@SuppressWarnings("unchecked")
public final class ServiceProvider {
    private static final Map<Class<?>, Object> registeredServices = new HashMap<>();

    /**
     * Registers an implementation to use for the specified service.
     *
     * @param clazz The class of the service that shall be registered.
     * @param service The implementation of {@code T} to register.
     *
     * @return The previously registered implementation or {@code null} if no
     *         implementation of {@code T} was registered before.
     *
     * @throws NullPointerException if {@code clazz} or {@code service} is
     *         {@code null}.
     */
    public static <T> T register(Class<?> clazz, T service) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(service, "service must not be null");

        return (T) registeredServices.put(clazz, service);
    }

    /**
     * Queries whether an implementation of the specified service is registered.
     *
     * @param clazz The service to query.
     *
     * @return {@code true} if an implementation is registered; otherwise,
     *         {@code false}.
     *
     * @throws NullPointerException if {@code clazz} is {@code null}.
     */
    public static boolean has(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        return registeredServices.containsKey(clazz);
    }

    /**
     * Gets the implementation of the specified service.
     *
     * @param clazz The service where to get the implementation for.
     *
     * @return The implementation of {@code clazz}.
     *
     * @throws NullPointerException if {@code clazz} is {@code null}.
     * @throws IllegalArgumentException if no implementation of {@code clazz} is
     *         registered.
     */
    public static <T> T get(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");

        T service = (T) registeredServices.get(clazz);

        if (service == null)
            throw new IllegalArgumentException("No service of specified class registered.");

        return service;
    }
}
