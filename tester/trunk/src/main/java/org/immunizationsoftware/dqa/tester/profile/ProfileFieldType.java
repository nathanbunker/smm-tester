package org.immunizationsoftware.dqa.tester.profile;

public enum ProfileFieldType {
	SEGMENT, SEGMENT_GROUP, FIELD, FIELD_PART, FIELD_SUB_PART, FIELD_VALUE, FIELD_PART_VALUE, FIELD_SUB_PART_VALUE, DATA_TYPE, DATA_TYPE_FIELD;
	public String toString() {
		if (this == SEGMENT) {
			return "Segment";
		}
		if (this == SEGMENT_GROUP) {
			return "Segment Group";
		}
		if (this == FIELD) {
			return "Field";
		}
		if (this == FIELD_PART) {
			return "Field Part";
		}
		if (this == FIELD_SUB_PART) {
			return "Field Sub Part";
		}
		if (this == FIELD_VALUE) {
			return "Field Value";
		}
		if (this == FIELD_PART_VALUE) {
			return "Field Part Value";
		}
		if (this == FIELD_SUB_PART_VALUE) {
			return "Field Sub Part Value";
		}
		if (this == DATA_TYPE) {
			return "Data Type";
		}
		if (this == DATA_TYPE_FIELD) {
			return "Data Type Field";
		}
		return super.toString();
	};
}
