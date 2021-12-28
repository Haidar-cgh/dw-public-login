package cn.com.dwsoft.login.process.zxtapp.packet.pojo;

import lombok.Data;

@Data
public class PackageDO {
  private int id;
  private String province;
  private String city;
  private String operators;
  private String packageLable;
  private String recommend;
  private String packageName;
  private String tariffType;
  private Long gearValue;
  private Long tariffDiscount;
  private Long universalFlow;
  private Long directionalFlow;
  /*private String directionalMusicFlow;
  private String directionalVideoFlow;
  private String directionalGameFlow;
  private String directionalShopFlow;*/
  private Long universalVoice;
  private String seneralMsg;
  private Long secondaryCard;
  private Long secondaryCardCount;
  private Long broadbandBandwidth;
  private String contractPeriod;
  private String feeDescription;
  private String handlingMethod;
  private String mesgHandlingNumber;
  private String mesgHandlingInstructions;
  private String packageStatus;
  private String packageEffectiveTime;
  private String packageExprieTime;
  private String packageNotes;
  private String packageType;
  private String conditions;
  private String hotSale;
  private String discountValue;
}
