package com.planet_ink.coffee_mud.utils;
import com.planet_ink.coffee_mud.interfaces.*;
import com.planet_ink.coffee_mud.common.*;
import java.util.*;


/* 
   Copyright 2000-2005 Bo Zimmerman

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
public class CMLister
{
	private CMLister(){};
	
    public static String itemSeenString(MOB viewer, 
                                        Environmental item, 
                                        boolean useName, 
                                        boolean longLook)
    {
        if(useName)
            return Util.capitalizeFirstLetter(item.name());
        else
        if((longLook)&&(item instanceof Item)&&(((Item)item).container()!=null))
            return Util.capitalizeFirstLetter("     "+item.name());
        else
        if(!item.name().equals(item.Name()))
            return Util.capitalizeFirstLetter(item.name()+" is here.");
        else
        if(item instanceof MOB)
            return Util.capitalizeFirstLetter(((MOB)item).displayText(viewer));
        else
        if(item.displayText().length()>0)
            return Util.capitalizeFirstLetter(item.displayText());
        else
            return Util.capitalizeFirstLetter(item.name());
    }
    
    public static int getReps(Environmental item, 
                              Vector theRest, 
                              MOB mob, 
                              boolean useName, 
                              boolean longLook)
    {
        String str=itemSeenString(mob,item,useName,longLook);
        String str2=null;
        int reps=0;
        int here=0;
        Environmental item2=null;
        while(here<theRest.size())
        {
            item2=(Environmental)theRest.elementAt(here);
            str2=itemSeenString(mob,item2,useName,longLook);
            if(str2.length()==0)
                theRest.removeElement(item2);
            else
            if((str.equals(str2))
            &&(Sense.seenTheSameWay(mob,item,item2)))
            {
                reps++;
                theRest.removeElement(item2);
            }
            else
                here++;
        }
        return reps;
    }
    
    public static void appendReps(int reps, StringBuffer say, boolean compress)
    {
        if(compress)
        {
            if(reps>0) 
                say.append("("+(reps+1)+") ");
        }
        else
        if(reps==0) say.append("      ");
        else
        if(reps>=99)
            say.append("("+Util.padLeftPreserve(""+(reps+1),3)+") ");
        else
        if(reps>0)
            say.append(" ("+Util.padLeftPreserve(""+(reps+1),2)+") ");
    }
    
    public static StringBuffer lister(MOB mob, 
                                      Vector things,
                                      boolean useName, 
                                      String tag,
                                      String tagParm,
                                      boolean longLook,
                                      boolean compress)
	{
	    boolean nameTagParm=((tagParm!=null)&&(tagParm.indexOf("*")>=0));
		StringBuffer say=new StringBuffer("");
        Environmental item=null;
		while(things.size()>0)
		{
			item=(Environmental)things.elementAt(0);
            things.removeElement(item);
            int reps=getReps(item,things,mob,useName,longLook);
			if(Sense.canBeSeenBy(item,mob)
			&&((item.displayText().length()>0)
			    ||Util.bset(mob.getBitmap(),MOB.ATT_SYSOPMSGS)
				||useName))
			{
                appendReps(reps,say,compress);
				if(Util.bset(mob.getBitmap(),MOB.ATT_SYSOPMSGS))
					say.append("^H("+CMClass.className(item)+")^N ");
                if((!compress)&&(!mob.isMonster())&&(mob.session().clientTelnetMode(Session.TELNET_MXP)))
                    say.append(CommonStrings.mxpImage(item," H=10 W=10",""," "));
				say.append("^I");
				
				if(tag!=null)
				{
				    if(nameTagParm)
					    say.append("^<"+tag+Util.replaceAll(tagParm,"*",item.name())+"^>");
				    else
				        say.append("^<"+tag+tagParm+"^>");
				}
                if(compress) say.append(Sense.colorCodes(item,mob)+"^I");
                say.append(Util.endWithAPeriod(itemSeenString(mob,item,useName,longLook)));
				if(tag!=null)
				    say.append("^</"+tag+"^>");
				if(!compress) 
                    say.append(Sense.colorCodes(item,mob)+"^N\n\r");
                else 
                    say.append("^N");
                
                if((longLook)
                &&(item instanceof Container)
                &&(((Container)item).container()==null)
                &&(((Container)item).isOpen())
                &&(!((Container)item).hasALid())
                &&(!Sense.canBarelyBeSeenBy(item,mob)))
                {
                    Vector V=((Container)item).getContents();
                    Item item2=null;
                    if(compress&&V.size()>0) say.append("{");
                    while(V.size()>0)
                    {
                        item2=(Item)V.firstElement();
                        int reps2=getReps(item2,V,mob,useName,false);
                        if(Sense.canBeSeenBy(item2,mob)
                        &&((item2.displayText().length()>0)
                            ||Util.bset(mob.getBitmap(),MOB.ATT_SYSOPMSGS)
                            ||(useName)))
                        {
                            if(!compress) say.append("      ");
                            appendReps(reps2,say,compress);
                            if((!compress)&&(!mob.isMonster())&&(mob.session().clientTelnetMode(Session.TELNET_MXP)))
                                say.append(CommonStrings.mxpImage(item," H=10 W=10",""," "));
                            say.append("^I");
                            if(compress)say.append(Sense.colorCodes(item2,mob)+"^I");
                            say.append(Util.endWithAPeriod(itemSeenString(mob,item2,useName,longLook)));
                            if(!compress) 
                                say.append(Sense.colorCodes(item2,mob)+"^N\n\r");
                            else
                                say.append("^N");
                        }
                        if(compress&&(V.size()==0)) say.append("} ");
                    }
                }
			}
		}
		return say;
	}
	
	public static StringBuffer reallyList(Hashtable these, int ofType)
	{
		return reallyList(these,ofType,null);
	}
	public static StringBuffer reallyList(Hashtable these)
	{
		return reallyList(these,-1,null);
	}
	public static StringBuffer reallyList(Hashtable these, Room likeRoom)
	{
		return reallyList(these,-1,likeRoom);
	}
	public static StringBuffer reallyList(Vector these, int ofType)
	{
		return reallyList(these.elements(),ofType,null);
	}
	public static StringBuffer reallyList(Enumeration these, int ofType)
	{
		return reallyList(these,ofType,null);
	}
	public static StringBuffer reallyList(Vector these)
	{
		return reallyList(these.elements(),-1,null);
	}
	public static StringBuffer reallyList(Enumeration these)
	{
		return reallyList(these,-1,null);
	}
	public static StringBuffer reallyList(Vector these, Room likeRoom)
	{
		return reallyList(these.elements(),-1,likeRoom);
	}
	public static StringBuffer reallyList(Hashtable these, int ofType, Room likeRoom)
	{
		StringBuffer lines=new StringBuffer("");
		if(these.size()==0) return lines;
		int column=0;
		for(Enumeration e=these.keys();e.hasMoreElements();)
		{
			String thisOne=(String)e.nextElement();
			Object thisThang=these.get(thisOne);
			String list=null;
			if(thisThang instanceof String)
				list=(String)thisThang;
			else
            if(thisThang instanceof CharClass)
                list=((CharClass)thisThang).ID()+(((CharClass)thisThang).isGeneric()?"*":"");
            else
            if(thisThang instanceof Race)
                list=((Race)thisThang).ID()+(((Race)thisThang).isGeneric()?"*":"");
            else
				list=CMClass.className(thisThang);
			if(ofType>=0)
			{
				if((thisThang!=null)&&(thisThang instanceof Ability))
				{
					if((((Ability)thisThang).classificationCode()&Ability.ALL_CODES)!=ofType)
						list=null;
				}
			}
			if((likeRoom!=null)&&(thisThang instanceof Room))
			{
				if((((Room)thisThang).roomID().length()>0)&&(!((Room)thisThang).getArea().Name().equals(likeRoom.getArea().Name())))
				   list=null;
			}
			if(list!=null)
			{
				if(++column>3)
				{
					lines.append("\n\r");
					column=1;
				}
				lines.append(Util.padRight(list,24)+" ");
			}
		}
		lines.append("\n\r");
		return lines;
	}

	public static StringBuffer reallyList(Vector these, int ofType, Room likeRoom)
	{ return reallyList(these.elements(),ofType,likeRoom);}
	public static StringBuffer reallyList(Enumeration these, Room likeRoom)
	{ return reallyList(these,-1,likeRoom);}
	public static StringBuffer reallyList(Enumeration these, int ofType, Room likeRoom)
	{
		StringBuffer lines=new StringBuffer("");
		if(!these.hasMoreElements()) return lines;
		int column=0;
		for(Enumeration e=these;e.hasMoreElements();)
		{
			Object thisThang=e.nextElement();
			String list=null;
			if(thisThang instanceof String)
				list=(String)thisThang;
            else
            if(thisThang instanceof CharClass)
                list=((CharClass)thisThang).ID()+(((CharClass)thisThang).isGeneric()?"*":"");
            else
            if(thisThang instanceof Race)
                list=((Race)thisThang).ID()+(((Race)thisThang).isGeneric()?"*":"");
            else
                list=CMClass.className(thisThang);
			if(ofType>=0)
			{
				if((thisThang!=null)&&(thisThang instanceof Ability))
				{
					if((((Ability)thisThang).classificationCode()&Ability.ALL_CODES)!=ofType)
						list=null;
				}
			}
			if((likeRoom!=null)&&(thisThang instanceof Room))
			{
				if((((Room)thisThang).roomID().length()>0)&&(!((Room)thisThang).getArea().Name().equals(likeRoom.getArea().Name())))
				   list=null;
			}
			if(list!=null)
			{
				if(++column>3)
				{
					lines.append("\n\r");
					column=1;
				}
				lines.append(Util.padRight(list,24)+" ");
			}
		}
		lines.append("\n\r");
		return lines;
	}
	public static StringBuffer reallyList2Cols(Enumeration these, int ofType, Room likeRoom)
	{
		StringBuffer lines=new StringBuffer("");
		if(!these.hasMoreElements()) return lines;
		int column=0;
		for(Enumeration e=these;e.hasMoreElements();)
		{
			Object thisThang=e.nextElement();
			String list=null;
			if(thisThang instanceof String)
				list=(String)thisThang;
            else
            if(thisThang instanceof CharClass)
                list=((CharClass)thisThang).ID()+(((CharClass)thisThang).isGeneric()?"*":"");
            else
            if(thisThang instanceof Race)
                list=((Race)thisThang).ID()+(((Race)thisThang).isGeneric()?"*":"");
            else
                list=CMClass.className(thisThang);
			if(ofType>=0)
			{
				if((thisThang!=null)&&(thisThang instanceof Ability))
				{
					if((((Ability)thisThang).classificationCode()&Ability.ALL_CODES)!=ofType)
						list=null;
				}
			}
			if((likeRoom!=null)&&(thisThang instanceof Room))
			{
				if((((Room)thisThang).roomID().length()>0)&&(!((Room)thisThang).getArea().Name().equals(likeRoom.getArea().Name())))
				   list=null;
			}
			if(list!=null)
			{
				if(++column>2)
				{
					lines.append("\n\r");
					column=1;
				}
				lines.append(Util.padRight(list,37)+" ");
			}
		}
		lines.append("\n\r");
		return lines;
	}
	
	public static StringBuffer fourColumns(Vector reverseList)
	{ return fourColumns(reverseList,null);}
	public static StringBuffer fourColumns(Vector reverseList, String tag)
	{
		StringBuffer topicBuffer=new StringBuffer("");
		int col=0;
		String s=null;
		for(int i=0;i<reverseList.size();i++)
		{
			if((++col)>4)
			{
				topicBuffer.append("\n\r");
				col=1;
			}
			s=(String)reverseList.elementAt(i);
		    if((tag!=null)&&(tag.length()>0))
		        s="^<"+tag+"^>"+s+"^</"+tag+"^>";
			if(s.length()>18)
			{
				topicBuffer.append(Util.padRight(s,(18*2)+1)+" ");
				++col;
			}
			else
				topicBuffer.append(Util.padRight(s,18)+" ");
		}
		return topicBuffer;
	}
}
