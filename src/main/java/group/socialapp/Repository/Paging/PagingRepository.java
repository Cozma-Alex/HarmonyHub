package group.socialapp.Repository.Paging;


import group.socialapp.Domain.Entity;
import group.socialapp.Repository.Repository;

public interface PagingRepository<ID,
        E extends Entity<ID>>
        extends Repository<ID, E> {

    Page<E> findAll(Pageable pageable);
}
