package com.chpython.core;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREPluginInternalException;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;
import org.python.util.PythonInterpreter;

/**
 *
 */
public class Functions {

	@api
	public static class python extends AbstractFunction {

		public Class[] thrown() {
			return new Class[]{CREPluginInternalException.class};
		}

		public boolean isRestricted() {
			return true;
		}

		public Boolean runAsync() {
			return null;
		}

		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			String script = args[0].val();
			CArray env = new CArray(t);
			CArray toReturn = new CArray(t);
			if (args.length > 1) {
				env = Static.getArray(args[1], t);
			}
			if (args.length > 2) {
				toReturn = Static.getArray(args[2], t);
			}
			PythonInterpreter inter = new PythonInterpreter();
			try {
				for(String key : env.stringKeySet()){
					inter.set(key, Construct.GetPOJO(env.get(key, t)));
				}
				inter.exec(script);
			} catch(Exception e){
				throw new CREPluginInternalException(e.getMessage(), t, e);
			}
			CArray ret = CArray.GetAssociativeArray(t);
			for(Mixed key : toReturn.keySet()){
				String k = toReturn.get(key, t).val();
				Object var = inter.get(k, Object.class);
				ret.set(k, Construct.GetConstruct(var), t);
			}
			return ret;
		}

		public String getName() {
			return "python";
		}

		public Integer[] numArgs() {
			return new Integer[]{1, 2, 3};
		}

		public String docs() {
			return "array {script, environment, toReturn} Runs a python script. The script can set variables beforehand with the environment"
				+ " variable, which should be an associative array mapping variable names to values. Arrays are not directly supported,"
				+ " as everything is simply passed in as a string. Values can be returned from the script, by giving a list of named values"
				+ " to toReturn, which will cause those values to be returned as a part of the associative array returned.";
		}

		public Version since() {
			return MSVersion.V3_3_1;
		}

	}
}
