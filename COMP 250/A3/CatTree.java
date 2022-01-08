import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import New.CatInfo;
import New.CatTree.CatNode;
import New.CatTree.CatTreeIterator; 


public class CatTree implements Iterable<CatInfo>{
    public CatNode root;
    
    public CatTree(CatInfo c) {
        this.root = new CatNode(c);
    }
    
    private CatTree(CatNode c) {
        this.root = c;
    }
    
    
    public void addCat(CatInfo c)
    {
        this.root = root.addCat(new CatNode(c));
    }
    
    public void removeCat(CatInfo c)
    {
        this.root = root.removeCat(c);
    }
    
    public int mostSenior()
    {
        return root.mostSenior();
    }
    
    public int fluffiest() {
        return root.fluffiest();
    }
    
    public CatInfo fluffiestFromMonth(int month) {
        return root.fluffiestFromMonth(month);
    }
    
    public int hiredFromMonths(int monthMin, int monthMax) {
        return root.hiredFromMonths(monthMin, monthMax);
    }
    
    public int[] costPlanning(int nbMonths) {
        return root.costPlanning(nbMonths);
    }
    
    
    
    public Iterator<CatInfo> iterator()
    {
        return new CatTreeIterator();
    }
    
    
    class CatNode {
        
        CatInfo data;
        CatNode senior;
        CatNode same;
        CatNode junior;
        
        public CatNode(CatInfo data) {
            this.data = data;
            this.senior = null;
            this.same = null;
            this.junior = null;
        }
        
        public String toString() {
            String result = this.data.toString() + "\n";
            if (this.senior != null) {
                result += "more senior " + this.data.toString() + " :\n";
                result += this.senior.toString();
            }
            if (this.same != null) {
                result += "same seniority " + this.data.toString() + " :\n";
                result += this.same.toString();
            }
            if (this.junior != null) {
                result += "more junior " + this.data.toString() + " :\n";
                result += this.junior.toString();
            }
            return result;
        }
        
        
        public CatNode addCat(CatNode c) {
        	CatNode subroot = this;
        	
        	if (c == null) {
        		return this;
        	}
        	
        	if (c.data.monthHired > subroot.data.monthHired) {
        		if (subroot.junior == null) {
        			subroot.junior =  c;
        		} else {
        			subroot = subroot.junior;
        			subroot.addCat(c);
        			}
        	} else if (c.data.monthHired < subroot.data.monthHired) {
        		if (subroot.senior == null) {
        			subroot.senior = c;
        		} else {
        			subroot = subroot.senior;
        			subroot.addCat(c);
        		}
        	} else {
        		if (c.data.furThickness > subroot.data.furThickness) {
        			CatNode  tmp = new CatNode(subroot.data);
        			subroot.data = c.data;
        			c.data = tmp.data;
        			
        			if (subroot.same == null) {
        				subroot.same = c;
        			} else {
        				subroot = subroot.same;
        				subroot.addCat(c);
        				
        			}
        		} else {
        			if (subroot.same == null) {
        				subroot.same = c;
        			} else {
        				subroot = subroot.same;
        				subroot.addCat(c);
        			}
        		}
        	}
        	return this;
        }
        
        
        public CatNode removeCat(CatInfo c) {
        	CatNode subroot = this;
        	if (c.monthHired > subroot.data.monthHired) {
        		if (subroot.junior != null) {
        			if (subroot.junior.data.equals(c)) {
        				if (subroot.junior.same != null) {
        					subroot.junior.data = subroot.junior.same.data;
        					subroot.junior.same = subroot.junior.same.same;
            				
            			} else if (subroot.junior.senior != null) {
            				CatNode junroot = subroot.junior.junior;
            				subroot.junior.data = subroot.junior.senior.data;
            				subroot.junior.same = subroot.junior.senior.same;
            				subroot.junior.junior = subroot.junior.senior.junior;
            				subroot.junior.senior = subroot.junior.senior.senior;
            				subroot.junior.addCat(junroot);
            				
            				
            				
            			} else if (subroot.junior.junior != null) {
            				subroot.junior.data = subroot.junior.junior.data;
            				subroot.junior.same = subroot.junior.junior.same;
            				subroot.junior.junior = subroot.junior.junior.junior;
            				//subroot.junior.senior = subroot.junior.junior.senior;
            				
            			} else {
            				subroot.junior = null;
        				}
        				
        			} else {
        				subroot = subroot.junior;
        				subroot.removeCat(c);
        			}
        		}
        	} else if (c.monthHired < subroot.data.monthHired) {
        		if (subroot.senior != null) {
        			if (subroot.senior.data.equals(c)) {
        				if (subroot.senior.same != null) {
        					subroot.senior.data = subroot.senior.same.data;
        					subroot.senior.same = subroot.senior.same.same;
            				
            			} else if (subroot.senior.senior != null) {
            				CatNode junroot = subroot.senior.junior;
            				subroot.senior.data = subroot.senior.senior.data;
            				subroot.senior.same = subroot.senior.senior.same;
            				subroot.senior.junior = subroot.senior.senior.junior;
            				subroot.senior.senior = subroot.senior.senior.senior;
            				subroot.senior.addCat(junroot);
            				//subroot.junior = subroot.addCat(c)
            				
            				
            			} else if (subroot.senior.junior != null) {
            				subroot.senior.data = subroot.senior.junior.data;
            				subroot.senior.same = subroot.senior.junior.same;
            				subroot.senior.junior = subroot.senior.junior.junior;
            				//subroot.senior.senior = subroot.senior.junior.senior;
            				
            			} else {
            				subroot.senior = null;
        				}
        				
        			} else {
        				subroot = subroot.senior;
        				subroot.removeCat(c);
        			}
        		}
        	} else {
        		if (subroot.data.equals(c)) {
        			if (subroot.same != null) {
        				subroot.data = subroot.same.data;
        				subroot.same = subroot.same.same;
        				
        			} else if (subroot.senior != null) {
        				CatNode junroot = subroot.junior;
        				subroot.data = subroot.senior.data;
        				subroot.same = subroot.senior.same;
        				subroot.junior = subroot.senior.junior;
        				subroot.senior = subroot.senior.senior;
        				subroot.addCat(junroot);
        				
        				
        			} else if (subroot.junior != null) {
        				subroot.data = subroot.junior.data;
        				subroot.same = subroot.junior.same;
        				subroot.junior = subroot.junior.junior;
        				//subroot.senior = subroot.junior.senior;
        				
        				
        			} else {
        				subroot = null;
	
        		}
        			
        		}
        		if (subroot.same != null) {
        			if (subroot.same.data.equals(c)) {
        				if (subroot.same.same != null) {
        					subroot.same.data = subroot.same.same.data;
        					subroot.same.same = subroot.same.same.same;
            				
            			} else if (subroot.same.senior != null) {
            				//CatNode junroot = new CatNode(subroot.same.junior.data);
            				CatNode junroot = subroot.same.junior;
            				subroot.same.data = subroot.same.senior.data;
            				subroot.same.same = subroot.same.senior.same;
            				subroot.same.junior = subroot.same.senior.junior;
            				subroot.same.senior = subroot.same.senior.senior;
            				subroot.same.addCat(junroot);
            				//subroot.junior = subroot.addCat(c)
            				
            				
            			} else if (subroot.same.junior != null) {
            				subroot.same.data = subroot.same.junior.data;
            				subroot.same.same = subroot.same.junior.same;
            				subroot.same.junior = subroot.same.junior.junior;
            				//subroot.same.senior = subroot.same.junior.senior;
            				
            			} else {
            				subroot.same = null;
        				}
        				
        			} else {
        				subroot = subroot.same;
        				subroot.removeCat(c);
        			}
        		}
        	}
        	return this;
        }
        
        
        public int mostSenior() {
        	int months = 0;
        	CatNode subroot = this;
        	if (subroot.senior != null) {
        		subroot = subroot.senior;
        		months = subroot.mostSenior();        	
        	} else {
        		months = subroot.data.monthHired;
        	}
            
            return months;
        }
        
        public int fluffiest() {
        	int thickness = this.data.furThickness;
        	
        	
        	if (this.junior != null) {
        		if (this.junior.data.furThickness > thickness) {
        			thickness = this.junior.data.furThickness;
        		}
        		if (this.junior.fluffiest() > thickness) {
        			thickness = this.junior.fluffiest();
        		}
        	}
        	if (this.senior != null) {
        		if (this.senior.data.furThickness > thickness) {
        			thickness = this.senior.data.furThickness;
        		}
        		if (this.senior.fluffiest() > thickness) {
        			thickness = this.senior.fluffiest();
        		}
        	}
        	return thickness;
        }
        
        
        public int hiredFromMonths(int monthMin, int monthMax) {
        	int realNum = 0;
        	
        	if (monthMin > monthMax || root == null) {
        		return 0;
        	}
        	if ((this.data.monthHired >= monthMin) && (this.data.monthHired <= monthMax)) {
        			realNum += 1;
        		}
        	if (this.same != null) {
        				
        		realNum += this.same.hiredFromMonths(monthMin, monthMax);
        		}
        	if (this.senior != null) {
        				
        		realNum += this.senior.hiredFromMonths(monthMin, monthMax);
        		}
        	if (this.junior != null) {
        				
        		realNum += this.junior.hiredFromMonths(monthMin, monthMax);
        		}
        		
        	
        	return realNum;
        }
            

        
        public CatInfo fluffiestFromMonth(int month) {
        	CatNode subroot = this;
            CatInfo catdata = null;
        	
            if (this.data.monthHired > month) {
            	if (this.senior != null) {
            		subroot = subroot.senior;
            		catdata = this.senior.fluffiestFromMonth(month);
            	}
            } else if (this.data.monthHired < month) {
            	if (this.junior != null) {
            		subroot = subroot.junior;
            		catdata = this.junior.fluffiestFromMonth(month);
            	}
            } else if (this.data.monthHired == month) {
            	catdata = this.data;
            }
            return catdata; 
        }
        
        public int[] costPlanning(int nbMonths) {
        	int i = 243;
            int[] monthsArray = new int[nbMonths];
            CatNode subroot = this;
            
            
            int index = 0;
            for (int j = i; j<i+nbMonths; j++) {
            	int costpermonth = 0;
            	CatTreeIterator iterator = new CatTreeIterator(subroot);
            	
            	for (int k = 0; k<subroot.hiredFromMonths(0, 1000); k++ ) {
            		CatInfo info = iterator.next();
            		if (info.nextGroomingAppointment == j) {
            			costpermonth += info.expectedGroomingCost;
            		}
            		
            	}
            	
            	
            	monthsArray[index] = costpermonth;
            	index ++;
            		
            }
            
            return monthsArray; 
        }
    }
        
    
    
    private class CatTreeIterator implements Iterator<CatInfo> {
    	private ArrayList<CatInfo> array;
    	private Iterator<CatInfo> iter;
    	
    	private void traverse(CatNode info) {
    		if (info != null && info.senior != null) {
    			traverse(info.senior);
    		}
    		if (info != null && info.same != null) {
    			traverse(info.same);
    		}
    		
    		array.add(info.data);
    		
    		if (info != null && info.junior != null) {
    			traverse(info.junior);
    		}
    		
    	}
        
    	public CatTreeIterator() {
        	array = new ArrayList<CatInfo>();
    		traverse(root);
    		iter = array.iterator();
        }
    	private CatTreeIterator(CatNode node) {
    		array = new ArrayList<CatInfo>();
    		traverse(node);
    		iter = array.iterator();
    	}
        
        public CatInfo next(){
        	return iter.next();
        }
        
        public boolean hasNext() {
        	return iter.hasNext();
        }
    
    }

}

    
 
