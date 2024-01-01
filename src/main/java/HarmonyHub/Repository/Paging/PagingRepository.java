package HarmonyHub.Repository.Paging;

import HarmonyHub.Models.Entity;
import HarmonyHub.Repository.Repository;

public interface PagingRepository<ID,
        E extends Entity<ID>>
        extends Repository<ID, E> {

    Page<E> findAll(Pageable pageable);
}
