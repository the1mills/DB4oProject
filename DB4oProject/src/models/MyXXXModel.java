package models;

import java.lang.reflect.Field;
import java.util.Vector;

import annotations.EditableField;
import annotations.ViewableField;

public class MyXXXModel extends DB4oModel {

	@EditableField
	private String sellout;
	@EditableField
	private String deathMonkey;
	@EditableField
	private String shutTrap;
	@EditableField
	private String mellowShit;
	@EditableField
	private String fuckThisMonkey;
	@EditableField
	private String shitTabaggon;
	@EditableField
	private String helloDeathShit;
	@EditableField
	private String fudgeMonkey;
	@EditableField
	private String sillyDeathFuck;
	@EditableField
	private String jelloPudding;
	@EditableField
	private String cellophane;
	@EditableField
	private String shizzleNizzle;
	@EditableField
	private String helloDeathShit1;
	@EditableField
	private String fudgeMonkey1;
	@EditableField
	private String sillyDeathFuck1;
	@EditableField
	private String jelloPudding1;
	@EditableField
	private String cellophane1;
	@EditableField
	private String shizzleNizzle1;

	public MyXXXModel() {

		super();
		canAddNewRecordInObjectViewer = true;
		canEditRecordInObjectViewer = true;
	}

	@Override
	public Vector<Object> getFieldDropDownValues(Field f) {
		Vector<Object> vo = new Vector<Object>();

		try {
			if (f == DB4oModel.class.getField("userDefinedFileName")) {
				vo.add(new String("candy"));
				vo.add(new String("death"));
				return vo;
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getSellout() {
		return sellout;
	}

	public void setSellout(String sellout) {
		this.sellout = sellout;
	}

	public String getDeathMonkey() {
		return deathMonkey;
	}

	public void setDeathMonkey(String deathMonkey) {
		this.deathMonkey = deathMonkey;
	}

	public String getShutTrap() {
		return shutTrap;
	}

	public void setShutTrap(String shutTrap) {
		this.shutTrap = shutTrap;
	}

	public String getMellowShit() {
		return mellowShit;
	}

	public void setMellowShit(String mellowShit) {
		this.mellowShit = mellowShit;
	}

	public String getFuckThisMonkey() {
		return fuckThisMonkey;
	}

	public void setFuckThisMonkey(String fuckThisMonkey) {
		this.fuckThisMonkey = fuckThisMonkey;
	}

	public String getShitTabaggon() {
		return shitTabaggon;
	}

	public void setShitTabaggon(String shitTabaggon) {
		this.shitTabaggon = shitTabaggon;
	}

	public String getHelloDeathShit() {
		return helloDeathShit;
	}

	public void setHelloDeathShit(String helloDeathShit) {
		this.helloDeathShit = helloDeathShit;
	}

	public String getFudgeMonkey() {
		return fudgeMonkey;
	}

	public void setFudgeMonkey(String fudgeMonkey) {
		this.fudgeMonkey = fudgeMonkey;
	}

	public String getSillyDeathFuck() {
		return sillyDeathFuck;
	}

	public void setSillyDeathFuck(String sillyDeathFuck) {
		this.sillyDeathFuck = sillyDeathFuck;
	}

	public String getJelloPudding() {
		return jelloPudding;
	}

	public void setJelloPudding(String jelloPudding) {
		this.jelloPudding = jelloPudding;
	}

	public String getCellophane() {
		return cellophane;
	}

	public void setCellophane(String cellophane) {
		this.cellophane = cellophane;
	}

	public String getShizzleNizzle() {
		return shizzleNizzle;
	}

	public void setShizzleNizzle(String shizzleNizzle) {
		this.shizzleNizzle = shizzleNizzle;
	}

	public String getHelloDeathShit1() {
		return helloDeathShit1;
	}

	public void setHelloDeathShit1(String helloDeathShit1) {
		this.helloDeathShit1 = helloDeathShit1;
	}

	public String getFudgeMonkey1() {
		return fudgeMonkey1;
	}

	public void setFudgeMonkey1(String fudgeMonkey1) {
		this.fudgeMonkey1 = fudgeMonkey1;
	}

	public String getSillyDeathFuck1() {
		return sillyDeathFuck1;
	}

	public void setSillyDeathFuck1(String sillyDeathFuck1) {
		this.sillyDeathFuck1 = sillyDeathFuck1;
	}

	public String getJelloPudding1() {
		return jelloPudding1;
	}

	public void setJelloPudding1(String jelloPudding1) {
		this.jelloPudding1 = jelloPudding1;
	}

	public String getCellophane1() {
		return cellophane1;
	}

	public void setCellophane1(String cellophane1) {
		this.cellophane1 = cellophane1;
	}

	public String getShizzleNizzle1() {
		return shizzleNizzle1;
	}

	public void setShizzleNizzle1(String shizzleNizzle1) {
		this.shizzleNizzle1 = shizzleNizzle1;
	}

}
