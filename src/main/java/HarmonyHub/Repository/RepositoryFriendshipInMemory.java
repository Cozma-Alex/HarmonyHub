package HarmonyHub.Repository;

import HarmonyHub.Models.Friendship;
import HarmonyHub.Models.Pair;
import HarmonyHub.Models.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class RepositoryFriendshipInMemory extends RepositoryUserInMemory<Pair<UUID, UUID>, Friendship> {

    RepositoryUserInMemory<UUID, User> user_repo;

    public RepositoryFriendshipInMemory(RepositoryUserInMemory<UUID, User> user_repo) {
        this.user_repo = user_repo;
    }

    @Override
    public Optional<Friendship> addOne(Friendship friendship) {

        friendship.setDateFriendship(LocalDateTime.now());
        return super.addOne(friendship);
    }

    @Override
    public Optional<Friendship> delete(Pair<UUID, UUID> id){
        return super.delete(id);
    }

}
