package com.example.demo.model;


import javax.persistence.*;

@Entity
public class Follow extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name="follower_id", nullable=false)
    private User follower;

    @ManyToOne
    @JoinColumn(name="followed_id", nullable=false)
    private User followed;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean deleted = false;


    public Follow(){};
    public Follow(User follower, User followed){
        this.follower = follower;
        this.followed = followed;
    }

    public void deleteRelation(){
        this.deleted = true;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }


    public Long getId() {
        return id;
    }

    public User getFollower() {
        return follower;
    }

    public User getFollowed() {
        return followed;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}
