package org.immunizationsoftware.dqa.tester.manager.hl7;

public enum ValueSet {
  HL70001("Sex"), HL70003("Event type"), HL70004("Patient class"), HL70005("Race"), HL70008("Acknowledgment Code"), HL70010(
      "Physician Id"), HL70061("Check digit scheme"), HL70063("Relationship"), HL70064("Financial class"), HL70076(
      "Message type"), HL70078("Abnormal flags"), HL70085("Observation result status codes interpretation"), HL70091(
      "Query priority"), HL70102("Delayed Acknowledgment type"), HL70103("Processing ID"), HL70104("Version ID"), HL70105(
      "Source of comment"), HL70119("Order Control Codes"), HL70125("Value type"), HL70126("Quantity limited request"), HL70136(
      "Yes/No indicator"), HL70155("Accept/Application acknowledgement conditions"), HL70162("Route of administration"), HL70163("Administration Site"), HL70189(
      "Ethnic Group"), HL70190("Address type"), HL70200("Name type"), HL70201("Telecommunications use code"), HL70202(
      "Telecommunication equipment type"), HL70203("Identifier type"), HL70204("Organizational name type"), HL70207(
      "Processing mode"), HL70208("Query response status"), HL70215("Publicity code"), HL70220("Living arrangement"), HL70227(
      "Manufacturers of vaccines"), HL70288("Census tract"), HL70289("County/parish"), HL70292(
      "Codes for Vaccines administered"), HL70296("Language"), HL70297("CN ID source"), HL70300("Namespace ID"), HL70301(
      "Universal ID type"), HL70322("Completion status"), HL70323("Action code"), HL70354("Message structure"), HL70356(
      "Alternate character set handling scheme"), HL70357("Message error status codes"), HL70360("Degree"), HL70361(
      "Application"), HL70362("Facility"), HL70363("Assigning Authority"), HL70396("Coding system"), HL70399("Country"), HL70441(
      "Immunization registry status"), HL70471("Query Name"), HL70516("Error Severity"), HL70533(
      "Application Error Code"), NIP001("Immunization information source"), NIP002("Substance refusal reason"), NIP003(
      "Observation identifiers"), CDCPHINVS("CDC PHIN Value Set"), CDCGI1VIS("CDC GI1 VIS"), CVX("CVX"), ISO("ISO+"), MVX("MVX"), UCUM("UCUM");
  private String title;

  private ValueSet(String title) {
    this.title = title;
  }
}
