package immibis.bon.gui;

public enum Operation {
	DeobfuscateMod("Deobfuscate mod", ".deobf"),
	ReobfuscateMod("Reobfuscate mod", ".reobf"),
	ReobfuscateModSRG("Reobfuscate mod to SRG", ".srg"),
	SRGifyMod("Deobfuscate mod to SRG", ".srg");
	
	Operation(String str, String suffix) {
		this.str = str;
		this.defaultNameSuffix = suffix;
	}

	public final String str;
	public final String defaultNameSuffix;
	
}
