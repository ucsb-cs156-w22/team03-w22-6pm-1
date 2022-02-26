package edu.ucsb.cs156.example.documents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureProperties {
    private float mag;
    private String place;
    private long time;
    private long updated;
    private int tz;
    private String url;
    private String detail;
    private int felt;
    private float cdi;
    private float mmi;
    private String alert;
    private String status;
    private int tsunami;
    private int sig;
    private String net;
    private String code;
    private String ids;
    private String sources;
    private String types;
    private int nst;
    private float dmin;
    private float rms;
    private float gap;
    private String magType;
    private String type;
    private String title;
}
