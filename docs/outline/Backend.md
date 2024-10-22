## API

| mapping                 | method | authority                                          |
|-------------------------|--------|----------------------------------------------------|
| /api/users              | GET    | ( $\geq$ Admin )                                   |
| /api/users              | POST   | ( $\geq$ Guest )                                   |
| /api/users/{id}         | GET    | ( $\geq$ User )                                    |
| /api/users/{id}         | PUT    | ( $\geq$ Admin ) OR ( owner of (User \[ id \]) )   |
| /api/users/{id}         | DELETE | ( $\geq$ Admin ) OR ( owner of (User \[ id \]) )   |
| /api/users/{id}/surveys | GET    | ( $\geq$ Guest )                                   |
| /api/users/{id}/votes   | GET    | ( $\geq$ Admin ) OR ( owner of (User \[ id \]) )   |
| /api/surveys            | GET    | ( $\geq$ Guest )                                   |
| /api/surveys            | POST   | ( $\geq$ User )                                    |
| /api/surveys/{id}       | GET    | ( $\geq$ Guest )                                   |
| /api/surveys/{id}       | PUT    | ( $\geq$ Admin )                                   |
| /api/surveys/{id}       | DELETE | ( $\geq$ Admin ) OR ( owner of (Survey \[ id \]) ) |
| /api/votes              | GET    | ( $\geq$ Admin ) OR ( owner of (Vote \[ id \]) )   |
| /api/votes              | POST   | ( $\geq$ Guest )                                   |
| /api/votes/{id}         | GET    | ( $\geq$ Admin ) OR ( owner of (Vote \[ id \]) )   |
| /api/votes/{id}         | PUT    | ( $\geq$ Admin ) OR ( owner of (Vote \[ id \]) )   |
| /api/votes/{id}         | DELETE | ( $\geq$ Admin ) OR ( owner of (Vote \[ id \]) )   |

## Domain

```mermaid
classDiagram
    class User {
        long id
        String username
    }
    class Survey {
        long id
        String title
        int voteTotal()
    }
    class Poll {
        long id
        int order
    }
    class Option {
        long id
        int order
        int voteCount()
    }
    class Vote {
        long id
        List~long~ options
    }
    
    Survey "1" *-- "1..n" Poll : Polls
    Poll "1" *-- "2..n" Option : Options
    
    Survey "0..*" --> "1" User : Author
    
    Vote "1" --> "0..*" Survey : Vote
    Vote "1" --> "0..*" User : Voter
```

## Data Transfer Objects
### User
```yaml
{
    id: (Number),
    username: "",
    ...(Authorization info)
}
```

### Survey
```yaml
{
    id: (Number),
    author: (User id),
    title: "",
    polls: [
        ...(Poll)
    ]
}
```

### Poll
```yaml
{
    id: (Number),
    question: "",
    options: [
        ...(Option)
    ],
    vote_total: (Number)
}
```

### Option
```yaml
{
    id: (Number),
    option: "",
    vote_count: (Number)
}
```

### Vote
```yaml
{
    id: (Number),
    voter: (User),
    survey: (Survey),
    polls: [
        poll: (Poll id),
        options: [
            ...(Option id)
        ]
    ]
}
```