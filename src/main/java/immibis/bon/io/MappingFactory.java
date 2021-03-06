package immibis.bon.io;

import immibis.bon.IProgressListener;
import immibis.bon.JoinMapping;
import immibis.bon.Mapping;
import immibis.bon.NameSet;
import immibis.bon.mcp.MappingLoader_MCP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MappingFactory {

	private static final Map<String, MappingLoader_MCP> mcpInstances = new HashMap<>();
	
	public static void registerMCPInstance(@NotNull String mcVersion, @NotNull NameSet.Side side, @NotNull File mcpPath, @Nullable IProgressListener progress) throws IOException {
		mcpInstances.put(mcVersion+" "+side, new MappingLoader_MCP(mcVersion, side, mcpPath, progress));
	}

	public static @NotNull Mapping getMapping(@NotNull NameSet from, @NotNull NameSet to, @Nullable IProgressListener progress) throws MappingUnavailableException {
		if(!from.mcVersion.equals(to.mcVersion))
			throw new MappingUnavailableException(from, to, "different Minecraft version");
		
		if(from.type == to.type)
			throw new MappingUnavailableException(from, to, "");
		
		MappingLoader_MCP mcpLoader = mcpInstances.get(from.mcVersion+" "+from.side);
		
		if(mcpLoader != null)
		{
			switch(from.type) {
			case MCP:
				switch(to.type) {
					case OBF: return new JoinMapping(mcpLoader.getReverseCSV(), mcpLoader.getReverseSRG());
					case SRG: return mcpLoader.getReverseCSV();
				}
				break;
			case OBF:
				switch(to.type) {
					case MCP: return new JoinMapping(mcpLoader.getForwardSRG(), mcpLoader.getForwardCSV());
					case SRG: return mcpLoader.getForwardSRG();
				}
				break;
			case SRG:
				switch(to.type) {
					case OBF: return mcpLoader.getReverseSRG();
					case MCP: return mcpLoader.getForwardCSV();
				}
				break;
			}
			throw new MappingUnavailableException(from, to, "not supported");
		}
		
		throw new MappingUnavailableException(from, to, "no known MCP folder for "+from.mcVersion);
	}

}
