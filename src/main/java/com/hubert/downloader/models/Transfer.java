package com.hubert.downloader.models;

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
