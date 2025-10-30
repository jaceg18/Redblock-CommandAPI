package com.ftjace.commandapi.Execution;

import java.util.function.Supplier;

/**
 * A record that holds the supplier.
 * @param supplier The supplier
 * @param <R> the return type intended.
 */
public record TabSupplier<R> (Supplier<R> supplier){
    /**
     * invokes supplier and returns tab completions.
     * @return tab completions.
     */
     R get() {return supplier.get();}
}
