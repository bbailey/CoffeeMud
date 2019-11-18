package com.planet_ink.coffee_mud.Races;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.core.collections.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

import java.util.*;

/*
   Copyright 2016-2019 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
public class Devil extends Unique
{
	@Override
	public String ID()
	{
		return "Devil";
	}

	private final static String localizedStaticName = CMLib.lang().L("Devil");

	@Override
	public String name()
	{
		return localizedStaticName;
	}

	@Override
	public int shortestMale()
	{
		return 72;
	}

	@Override
	public int shortestFemale()
	{
		return 70;
	}

	@Override
	public int heightVariance()
	{
		return 12;
	}

	@Override
	public int lightestWeight()
	{
		return 180;
	}

	@Override
	public int weightVariance()
	{
		return 50;
	}

	@Override
	public boolean fertile()
	{
		return true;
	}

	@Override
	public Weapon myNaturalWeapon()
	{
		if(naturalWeapon==null)
		{
			naturalWeapon=CMClass.getWeapon("StdWeapon");
			naturalWeapon.setName(L("claws"));
			naturalWeapon.setMaterial(RawMaterial.RESOURCE_BONE);
			naturalWeapon.setUsesRemaining(1000);
			naturalWeapon.setWeaponDamageType(Weapon.TYPE_PIERCING);
		}
		return naturalWeapon;
	}

	@Override
	public long forbiddenWornBits()
	{
		return 0;
	}

	private final String[]	racialEffectNames			= { "Prayer_TaintOfEvil" };
	private final int[]		racialEffectLevels			= { 1 };
	private final String[]	racialEffectParms			= { "" };

	@Override
	protected String[] racialEffectNames()
	{
		return racialEffectNames;
	}

	@Override
	protected int[] racialEffectLevels()
	{
		return racialEffectLevels;
	}

	@Override
	protected String[] racialEffectParms()
	{
		return racialEffectParms;
	}

	private final String[]	culturalAbilityNames			= { "Undercommon" };
	private final int[]		culturalAbilityProficiencies	= { 25 };

	private final static String localizedStaticRacialCat = CMLib.lang().L("Devil");

	@Override
	public String racialCategory()
	{
		return localizedStaticRacialCat;
	}

	@Override
	public String[] culturalAbilityNames()
	{
		return culturalAbilityNames;
	}

	@Override
	public int[] culturalAbilityProficiencies()
	{
		return culturalAbilityProficiencies;
	}

	// 									   an ey ea he ne ar ha to le fo no gi mo wa ta wi
	private static final int[]	parts	= { 0, 2, 2, 1, 1, 2, 2, 1, 2, 2, 1, 0, 1, 1, 1, 2 };

	@Override
	public int[] bodyMask()
	{
		return parts;
	}

	@Override
	public int availabilityCode()
	{
		return Area.THEME_FANTASY | Area.THEME_SKILLONLYMASK;
	}

	private final String[]	racialAbilityNames			= { "WingFlying" };
	private final int[]		racialAbilityLevels			= { 1 };
	private final int[]		racialAbilityProficiencies	= { 100 };
	private final boolean[]	racialAbilityQuals			= { false };
	private final String[]	racialAbilityParms			= { "" };

	@Override
	public String[] racialAbilityNames()
	{
		return racialAbilityNames;
	}

	@Override
	public int[] racialAbilityLevels()
	{
		return racialAbilityLevels;
	}

	@Override
	public int[] racialAbilityProficiencies()
	{
		return racialAbilityProficiencies;
	}

	@Override
	public boolean[] racialAbilityQuals()
	{
		return racialAbilityQuals;
	}

	@Override
	public String[] racialAbilityParms()
	{
		return racialAbilityParms;
	}

	@Override
	public void affectPhyStats(final Physical affected, final PhyStats affectableStats)
	{
		super.affectPhyStats(affected,affectableStats);
		affectableStats.setSensesMask(affectableStats.sensesMask()|PhyStats.CAN_SEE_INFRARED);
	}

	@Override
	public void affectCharStats(final MOB affectedMOB, final CharStats affectableStats)
	{
		super.affectCharStats(affectedMOB, affectableStats);
		affectableStats.setStat(CharStats.STAT_SAVE_FIRE,affectableStats.getStat(CharStats.STAT_SAVE_FIRE)+100);
		affectableStats.setStat(CharStats.STAT_SAVE_POISON,affectableStats.getStat(CharStats.STAT_SAVE_POISON)+100);
		affectableStats.setStat(CharStats.STAT_SAVE_ACID,affectableStats.getStat(CharStats.STAT_SAVE_ACID)+10);
		affectableStats.setStat(CharStats.STAT_SAVE_COLD,affectableStats.getStat(CharStats.STAT_SAVE_COLD)+10);
	}

	private static Vector<RawMaterial>	resources	= new Vector<RawMaterial>();

	@Override
	public List<RawMaterial> myResources()
	{
		synchronized(resources)
		{
			if(resources.size()==0)
			{
				resources.addElement(makeResource
				(L("a @x1 internal organ",name().toLowerCase()),RawMaterial.RESOURCE_MEAT));
				resources.addElement(makeResource
				(L("a @x1 horn",name().toLowerCase()),RawMaterial.RESOURCE_BONE));
			}
		}
		return resources;
	}
}
