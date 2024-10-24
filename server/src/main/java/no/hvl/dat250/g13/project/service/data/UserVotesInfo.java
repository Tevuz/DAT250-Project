package no.hvl.dat250.g13.project.service.data;

import java.util.List;

public record UserVotesInfo(
        Long userId,
        List<VoteInfo> votes
) {
}
