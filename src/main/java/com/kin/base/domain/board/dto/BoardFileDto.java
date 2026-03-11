package com.kin.base.domain.board.dto;

import com.kin.base.domain.board.entity.BoardFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardFileDto {

    private Long id;
    private String originName;
    private String saveName;
    private Long size;

    @Builder
    public BoardFileDto(BoardFile boardFile) {
        this.id = boardFile.getId();
        this.originName = boardFile.getOriginName();
        this.saveName = boardFile.getSaveName();
        this.size = boardFile.getSize();
    }
}
