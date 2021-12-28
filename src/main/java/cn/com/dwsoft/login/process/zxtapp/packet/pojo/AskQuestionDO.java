package cn.com.dwsoft.login.process.zxtapp.packet.pojo;


import lombok.Data;

@Data
public class AskQuestionDO {

  private int id;
  private String type;
  private String multi_select;
  private String text;
  private int option_num;
  private String option_a_text;
  private String option_a_p;
  private String option_a_t;
  private String option_a_v;
  private String option_b_text;
  private String option_b_p;
  private String option_b_t;
  private String option_b_v;
  private String option_c_text;
  private String option_c_p;
  private String option_c_t;
  private String option_c_v;
  private String option_d_text;
  private String option_d_p;
  private String option_d_t;
  private String option_d_v;
  private String option_e_text;
  private String option_e_p;
  private String option_e_t;
  private String option_e_v;
  private String options;
}
