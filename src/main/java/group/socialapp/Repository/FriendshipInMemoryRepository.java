package group.socialapp.Repository;

import group.socialapp.Domain.Friendship;
import group.socialapp.Domain.Pair;
import group.socialapp.Domain.User;

import java.time.LocalDateTime;
import java.util.Optional;

public class FriendshipInMemoryRepository extends InMemoryRepository<Pair<String, String >, Friendship>{

    InMemoryRepository<String, User> user_repo;

    public FriendshipInMemoryRepository(InMemoryRepository<String, User> user_repo) {
        this.user_repo = user_repo;
    }

    @Override
    public Optional<Friendship> addOne(Friendship friendship) {

        friendship.setDate(LocalDateTime.now());
        return super.addOne(friendship);
    }

    @Override
    public Optional<Friendship> delete(Pair<String, String> id){
        return super.delete(id);
    }
}
