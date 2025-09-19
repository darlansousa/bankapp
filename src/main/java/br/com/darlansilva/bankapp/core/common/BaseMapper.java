package br.com.darlansilva.bankapp.core.common;

public interface BaseMapper<E, D> {

    D toDomain(E entity);

    E toEntity(D domain);

}
