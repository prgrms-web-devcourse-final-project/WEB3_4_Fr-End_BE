package com.frend.planit.domain.image.entity;

import com.frend.planit.domain.image.type.HolderType;
import com.frend.planit.global.base.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Entity
@Table(indexes = @Index(name = "idx_holder", columnList = "holderType, holderId"))
public class Image extends BaseTime {

    @URL
    @Setter
    @Column(nullable = false)
    private String url;

    @Column
    private HolderType holderType;

    @Column
    private Long holderId;

    public void setHolder(HolderType holderType, long holderId) {
        this.holderType = holderType;
        this.holderId = holderId;
    }
}