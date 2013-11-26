package org.immunizationsoftware.dqa.tester.manager.hl7.messages;

import java.util.HashMap;
import java.util.Map;

import org.immunizationsoftware.dqa.tester.manager.hl7.Cardinality;
import org.immunizationsoftware.dqa.tester.manager.hl7.HL7Component;
import org.immunizationsoftware.dqa.tester.manager.hl7.ItemType;
import org.immunizationsoftware.dqa.tester.manager.hl7.UsageType;
import org.immunizationsoftware.dqa.tester.manager.hl7.datatypes.CE;
import org.immunizationsoftware.dqa.tester.manager.hl7.scenario.ScenarioChecker;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.ERR;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.IN1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.MSA;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.MSH;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.NK1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.NTE;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.OBX;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.ORC;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PD1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PIDforRSP;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.PV1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.QAK;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.QPD;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.RXAforRSP;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.RXR;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.TQ1;
import org.immunizationsoftware.dqa.tester.manager.hl7.segments.TQ2;

public class RSP extends HL7Component
{
  private MSH msh = null;
  private MSA msa = null;
  private ERR err = null;
  private QAK qak = null;
  private QPD qpd = null;
  private PatientGroup patientGroup = null;

  public MSH getMsh() {
    return msh;
  }

  public void setMsh(MSH msh) {
    this.msh = msh;
  }

  public MSA getMsa() {
    return msa;
  }

  public void setMsa(MSA msa) {
    this.msa = msa;
  }

  public ERR getErr() {
    return err;
  }

  public void setErr(ERR err) {
    this.err = err;
  }

  public QAK getQak() {
    return qak;
  }

  public void setQak(QAK qak) {
    this.qak = qak;
  }

  public QPD getQpd() {
    return qpd;
  }

  public void setQpd(QPD qpd) {
    this.qpd = qpd;
  }

  public PatientGroup getPatientGroup() {
    return patientGroup;
  }

  public void setPatientGroup(PatientGroup patientGroup) {
    this.patientGroup = patientGroup;
  }

  @Override
  public HL7Component makeAnother() {
    return new RSP(this);
  }

  public RSP(RSP copy) {
    super(copy);
    init();
  }

  public RSP() {
    super(ItemType.MESSAGE, "RSP", "Response", 6, UsageType.R, Cardinality.ONE_TIME_ONLY);
    init();
  }

  @Override
  public void init() {
    setChild(1, msh = new MSH(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(2, msa = new MSA(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(3, err = new ERR(UsageType.O, Cardinality.ZERO_OR_MORE));
    setChild(4, qak = new QAK(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(5, qpd = new QPD(UsageType.R, Cardinality.ONE_TIME_ONLY));
    setChild(6, patientGroup = new PatientGroup(UsageType.RE, Cardinality.ZERO_OR_MORE));
  }

  public RSP(boolean checkForecastPresent) {
    super(ItemType.MESSAGE, "RSP.cds", "Response with Evaluations and Recommendations", 6, UsageType.R,
        Cardinality.ONE_TIME_ONLY);
    init();
    scenarioChecker = new ScenarioChecker() {
      private final String[] SERIES_STATUS_INDICATORS = { "U", "I", "O", "S", "R" };

      public boolean checkScenario(HL7Component comp) {
        if (comp instanceof RSP) {
          boolean foundAtLeastOneForecast = false;
          boolean allVaccinationsHaveEvaluation = true;
          RSP rsp = (RSP) comp;
          RSP.PatientGroup patientGroup = rsp.getPatientGroup();
          RSP.PatientGroup.OrderGroup orderGroup = patientGroup.getOrderGroup();
          while (orderGroup != null) {
            if (orderGroup.getRxa().getAdministeredCode().getIdentifier().getValue().equals("998")) {
              // not administered
              Map<String, RSP.ForecastRecommendation> forecastRecommendationMap = new HashMap<String, RSP.ForecastRecommendation>();
              RSP.PatientGroup.OrderGroup.ObservationGroup observationGroup = orderGroup.getObservationGroup();
              while (observationGroup != null) {
                OBX obx = observationGroup.getObx();
                String obsId = obx.getObservationIdentifier().getIdentifier().getValue();
                String id = "order group #" + orderGroup.getCardinalityCount();
                if (!obx.getObservationSubID().isEmpty()) {
                  id += " sub id " + obx.getObservationSubID().getValue();
                }
                RSP.ForecastRecommendation forecastRecommendation = forecastRecommendationMap.get(id);
                if (forecastRecommendation == null) {
                  forecastRecommendationMap.put(id, forecastRecommendation = new RSP.ForecastRecommendation());
                  forecastRecommendation.id = id;
                }

                if (obsId.equals("30979-9")) {
                  forecastRecommendation.isValuedVaccinesDueNext = !obx.getObservationValue().isEmpty();
                } else if (obsId.equals("59779-9")) {
                  forecastRecommendation.isValuedVaccineScheduleUsed = !obx.getObservationValue().isEmpty();
                } else if (obsId.equals("59783-1")) {
                  forecastRecommendation.isValuedSeriesStatus = !obx.getObservationValue().isEmpty();
                  if (obx.getObservationValue() instanceof CE) {
                    CE ce = (CE) obx.getObservationValue();
                    forecastRecommendation.seriesStatus = ce.getIdentifier().getValue();
                  }
                } else if (obsId.equals("30980-7")) {
                  forecastRecommendation.isValuedDateVaccineDue = !obx.getObservationValue().isEmpty();
                } else if (obsId.equals("30981-5")) {
                  forecastRecommendation.isValuedEarliestDateToGive = !obx.getObservationValue().isEmpty();
                } else if (obsId.equals("30973-2")) {
                  forecastRecommendation.isValuedDoseNumberInSeries = !obx.getObservationValue().isEmpty();
                } else if (obsId.equals("59782-3")) {
                  forecastRecommendation.isValuedNumberOfDosesInPrimarySeries = !obx.getObservationValue().isEmpty();
                }
                orderGroup = (RSP.PatientGroup.OrderGroup) orderGroup.getNextComponent();
              }
              for (RSP.ForecastRecommendation forecastRecommendation : forecastRecommendationMap.values()) {
                if (forecastRecommendation.isValuedVaccinesDueNext
                    || forecastRecommendation.isValuedVaccineScheduleUsed
                    || forecastRecommendation.isValuedSeriesStatus) {
                  // this is a recommendation
                  foundAtLeastOneForecast = true;
                  String forSubIdText = "";
                  if (!forecastRecommendation.id.equals("")) {
                    forSubIdText = " for " + forecastRecommendation.id;
                  }
                  if (!forecastRecommendation.isValuedVaccinesDueNext) {
                    orderGroup.orc.addConformanceIssue(
                        "No OBX with recommendation - vaccines due next (LOINC 30979-9) was indicated" + forSubIdText,
                        "101", "7", "E", rsp.getConformanceIssueList());
                  }
                  if (!forecastRecommendation.isValuedVaccineScheduleUsed) {
                    orderGroup.orc.addConformanceIssue(
                        "No OBX with recommendation - vaccine schedule used (LOINC 59779-9) was indicated"
                            + forSubIdText, "101", "7", "E", rsp.getConformanceIssueList());
                  }
                  if (!forecastRecommendation.isValuedSeriesStatus) {
                    orderGroup.orc.addConformanceIssue(
                        "No OBX with recommendation - series status (LOINC 59783-1) was indicated" + forSubIdText,
                        "101", "7", "E", rsp.getConformanceIssueList());
                  } else {
                    boolean shouldHaveRecommendation = false;
                    for (String seriesStatusIndicator : SERIES_STATUS_INDICATORS) {
                      if (forecastRecommendation.seriesStatus.equals(seriesStatusIndicator)) {
                        shouldHaveRecommendation = true;
                        break;
                      }
                    }
                    if (shouldHaveRecommendation) {
                      if (!forecastRecommendation.isValuedDateVaccineDue) {
                        orderGroup.orc.addConformanceIssue(
                            "No OBX with recommendation - date vaccine due (LOINC 30980-7) was indicated"
                                + forSubIdText, "101", "7", "E", rsp.getConformanceIssueList());
                      }
                      if (!forecastRecommendation.isValuedEarliestDateToGive) {
                        orderGroup.orc.addConformanceIssue(
                            "No OBX with recommendation - earliest date to give (LOINC 30981-5) was indicated"
                                + forSubIdText, "101", "7", "W", rsp.getConformanceIssueList());
                      }
                      if (!forecastRecommendation.isValuedDoseNumberInSeries) {
                        orderGroup.orc.addConformanceIssue(
                            "No OBX with recommendation - dose number in series (LOINC 30973-2) was indicated"
                                + forSubIdText, "101", "7", "W", rsp.getConformanceIssueList());
                      }
                      if (!forecastRecommendation.isValuedNumberOfDosesInPrimarySeries) {
                        orderGroup.orc.addConformanceIssue(
                            "No OBX with recommendation - number of doses in primary series (LOINC 59782-3) was indicated"
                                + forSubIdText, "101", "7", "W", rsp.getConformanceIssueList());
                      }
                    }
                  }
                }
              }
            } else if (orderGroup.getRxa().getCompletionStatus().getValue().equals("CP")
                || orderGroup.getRxa().getCompletionStatus().getValue().equals("")) {
              // must have evaluation
              boolean isValuedVaccineType = false;
              boolean isValuedVaccineScheduleUsed = false;
              boolean isValuedDoseValidity = false;
              RSP.PatientGroup.OrderGroup.ObservationGroup observationGroup = orderGroup.getObservationGroup();
              while (observationGroup != null) {
                OBX obx = observationGroup.getObx();
                String obsId = obx.getObservationIdentifier().getIdentifier().getValue();

                if (obsId.equals("30956-7")) {
                  isValuedVaccineType = !obx.getObservationValue().isEmpty();
                } else if (obsId.equals("59779-9")) {
                  isValuedVaccineScheduleUsed = !obx.getObservationValue().isEmpty();
                } else if (obsId.equals("59781-5")) {
                  isValuedDoseValidity = !obx.getObservationValue().isEmpty();
                }
                observationGroup = (RSP.PatientGroup.OrderGroup.ObservationGroup) observationGroup.getNextComponent();
              }
              if (!isValuedVaccineType) {
                orderGroup.orc.addConformanceIssue(
                    "No OBX with evaluation - vaccine type (LOINC 30956-7) was indicated", "101", "7", "E",
                    rsp.getConformanceIssueList());
                allVaccinationsHaveEvaluation = false;
              }
              if (!isValuedVaccineScheduleUsed) {
                orderGroup.orc.addConformanceIssue(
                    "No OBX with evaluation - vaccine schedule used (LOINC 59779-9) was indicated", "101", "7", "E",
                    rsp.getConformanceIssueList());
                allVaccinationsHaveEvaluation = false;
              }
              if (!isValuedDoseValidity) {
                orderGroup.orc.addConformanceIssue(
                    "No OBX with evaluation - vaccine dose validity (LOINC 59781-5) was indicated", "101", "7", "E",
                    rsp.getConformanceIssueList());
                allVaccinationsHaveEvaluation = false;
              }
            }
            orderGroup = (RSP.PatientGroup.OrderGroup) orderGroup.getNextComponent();
          }
          if (!foundAtLeastOneForecast) {
            rsp.addConformanceIssue("No recommendations found, expecting them in order groups where RXA-5 = 998",
                "101", "7", "E", rsp.getConformanceIssueList());
          }
          return foundAtLeastOneForecast && allVaccinationsHaveEvaluation;
        }
        return false;
      }
    };
  }

  private class ForecastRecommendation
  {
    public String id = "";
    public boolean isValuedVaccinesDueNext = false;
    public boolean isValuedVaccineScheduleUsed = false;
    public boolean isValuedSeriesStatus = false;
    public String seriesStatus = null;
    public boolean isValuedDateVaccineDue = false;
    public boolean isValuedEarliestDateToGive = false;
    public boolean isValuedDoseNumberInSeries = false;
    public boolean isValuedNumberOfDosesInPrimarySeries = false;
  }

  public class PatientGroup extends HL7Component
  {

    private PIDforRSP pid = null;
    private PD1 pd1 = null;
    private NK1 nk1 = null;
    private PV1 pv1 = null;
    private IN1 in1 = null;
    private OrderGroup orderGroup = null;

    public PIDforRSP getPid() {
      return pid;
    }

    public void setPid(PIDforRSP pid) {
      this.pid = pid;
    }

    public PD1 getPd1() {
      return pd1;
    }

    public void setPd1(PD1 pd1) {
      this.pd1 = pd1;
    }

    public NK1 getNk1() {
      return nk1;
    }

    public void setNk1(NK1 nk1) {
      this.nk1 = nk1;
    }

    public PV1 getPv1() {
      return pv1;
    }

    public void setPv1(PV1 pv1) {
      this.pv1 = pv1;
    }

    public IN1 getIn1() {
      return in1;
    }

    public void setIn1(IN1 in1) {
      this.in1 = in1;
    }

    public OrderGroup getOrderGroup() {
      return orderGroup;
    }

    public void setOrderGroup(OrderGroup orderGroup) {
      this.orderGroup = orderGroup;
    }

    @Override
    public HL7Component makeAnother() {
      return new PatientGroup(this);
    }

    public PatientGroup(PatientGroup copy) {
      super(copy);
      init();
    }

    public PatientGroup(UsageType usageType, Cardinality cardinality) {
      super(ItemType.MESSAGE_PART, "PID", "RSP - Patient Group", 6, usageType, cardinality);
      init();
    }

    @Override
    public void init() {
      setChild(1, pid = new PIDforRSP(UsageType.R, Cardinality.ONE_TIME_ONLY));
      setChild(2, pd1 = new PD1(UsageType.RE, Cardinality.ZERO_OR_ONE));
      setChild(3, nk1 = new NK1(UsageType.RE, Cardinality.ZERO_OR_MORE));
      setChild(4, pv1 = new PV1(UsageType.O, Cardinality.ZERO_OR_ONE));
      setChild(5, in1 = new IN1(UsageType.O, Cardinality.ZERO_OR_ONE));
      setChild(6, orderGroup = new OrderGroup(UsageType.RE, Cardinality.ZERO_OR_MORE));
    }

    public class OrderGroup extends HL7Component
    {
      private ORC orc = null;
      private TQ1 tq1 = null;
      private TQ2 tq2 = null;
      private RXAforRSP rxa = null;
      private RXR rxr = null;
      private ObservationGroup observationGroup = null;

      public ORC getOrc() {
        return orc;
      }

      public void setOrc(ORC orc) {
        this.orc = orc;
      }

      public TQ1 getTq1() {
        return tq1;
      }

      public void setTq1(TQ1 tq1) {
        this.tq1 = tq1;
      }

      public TQ2 getTq2() {
        return tq2;
      }

      public void setTq2(TQ2 tq2) {
        this.tq2 = tq2;
      }

      public RXAforRSP getRxa() {
        return rxa;
      }

      public void setRxa(RXAforRSP rxa) {
        this.rxa = rxa;
      }

      public RXR getRxr() {
        return rxr;
      }

      public void setRxr(RXR rxr) {
        this.rxr = rxr;
      }

      public ObservationGroup getObservationGroup() {
        return observationGroup;
      }

      public void setObservationGroup(ObservationGroup observationGroup) {
        this.observationGroup = observationGroup;
      }

      @Override
      public HL7Component makeAnother() {
        return new OrderGroup(this);
      }

      public OrderGroup(OrderGroup copy) {
        super(copy);
        init();
      }

      public OrderGroup(UsageType usageType, Cardinality cardinality) {
        super(ItemType.MESSAGE_PART, "ORC", "RSP - Patient Group - Order Group", 6, usageType, cardinality);
        init();
      }

      public void init() {
        setChild(1, orc = new ORC(UsageType.R, Cardinality.ONE_OR_MORE_TIMES));
        setChild(2, tq1 = new TQ1(UsageType.O, Cardinality.ZERO_OR_ONE));
        setChild(3, tq2 = new TQ2(UsageType.O, Cardinality.ZERO_OR_ONE));
        setChild(4, rxa = new RXAforRSP(UsageType.R, Cardinality.ONE_TIME_ONLY));
        setChild(5, rxr = new RXR(UsageType.O, Cardinality.ZERO_OR_ONE));
        setChild(6, observationGroup = new ObservationGroup(UsageType.RE, Cardinality.ZERO_OR_MORE));
      }

      public class ObservationGroup extends HL7Component
      {
        private OBX obx = null;
        private NTE nte = null;

        public OBX getObx() {
          return obx;
        }

        public void setObx(OBX obx) {
          this.obx = obx;
        }

        public NTE getNte() {
          return nte;
        }

        public void setNte(NTE nte) {
          this.nte = nte;
        }

        @Override
        public HL7Component makeAnother() {
          return new ObservationGroup(this);
        }

        public ObservationGroup(ObservationGroup copy) {
          super(copy);
          init();
        }

        public ObservationGroup(UsageType usageType, Cardinality cardinality) {
          super(ItemType.MESSAGE_PART, "OBX", "RSP - Patient Group - Order Group - Observation Group", 2, usageType,
              cardinality);
          init();
        }

        public void init() {
          setChild(1, obx = new OBX(UsageType.R, Cardinality.ONE_TIME_ONLY));
          setChild(2, nte = new NTE(UsageType.RE, Cardinality.ZERO_OR_MORE));
        }
      }

    }

  }

}
