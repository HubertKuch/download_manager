package com.hubert.downloader.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    private Float transfer;
    private Float startTransfer;
}
