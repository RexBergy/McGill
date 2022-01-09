
(* Section 1 : Lists *)

(* Question 1.1 : Most common element of *sorted* list *)

let mode_tests: (int list * int) list = [
  [1],1;
  [1 ;3 ;6;5; 4],1;
  [5; 2; 2; 5 ; 6 ; 1],2; 
  [1;2;2;3;3;3],3;
];;


let mode (l: 'a list) : 'a = 
  let l = List.sort compare l in
  let rec aux l ((cur_el, cur_num) : 'a * int) ((max_el, max_num) : 'a * int) =
    
    match l with 
    | [] -> max_el
    | h::t -> 
        if h = cur_el then 
          let cur_num = cur_num + 1 in
          if cur_num > max_num then
            let max_num = cur_num in
            let max_el = cur_el in
            aux t (cur_el, cur_num) (max_el, max_num)
          else
            aux t (cur_el, cur_num) (max_el, max_num)
        else 
          let cur_el = h in
          let cur_num = 1 in
          aux t (cur_el, cur_num) (max_el, max_num)
                   
  in
  let cur_el::rest = l  in
  aux rest (cur_el, 1) (cur_el, 1)
;;

(* Question 1.2 : Most common consecutive pairing *)

let pair_mode_tests: (int list * (int * int) ) list = [
  [1;2;3;2;4;3;2;5;3;2], (3,2);
  [1;2], (1,2);
] ;;

let pair_mode (l: 'a list) : 'a * 'a = 
  let rec make_pair l = match l with
    | x::xs::[] -> (x,xs)::[]
    | x::y::ys -> (x,y)::make_pair (y::ys)
  in 
  let l = make_pair l in
  mode l
;;


(* Section 2 : Custom data types *)

let convert_time ((from_unit, val_) : time_unit value) to_unit : time_unit value =
  match (from_unit, to_unit) with
  | (Second,Hour) -> (Hour,val_ /. (3600.))
  | (Hour,Second) -> (Second,val_ *. (3600.))
  | (Second,Second) -> (Second,val_)
  | (Hour,Hour) -> (Hour,val_)
;;

let convert_dist ((from_unit, val_) : dist_unit value) to_unit : dist_unit value = 
  match (from_unit, to_unit) with
  | (Foot,Meter) -> (Foot,val_ *. (0.3048))
  | (Foot,Mile) -> (Foot, val_ /. (5280.))
  | (Foot,Foot) -> (Foot, val_)
  | (Meter,Foot) -> (Meter, val_ /. (0.3048))
  | (Meter,Meter) -> (Meter, val_)
  | (Meter,Mile) -> (Meter,val_ /. (1609.344))
  | (Mile,Foot) -> (Mile, val_ *. (5280.))
  | (Mile,Meter) -> (Mile, val_ *. (1609.344))
  | (Mile,Mile) -> (Mile, val_)
;;

let convert_speed ((from_unit, val_) : speed_unit value) to_unit : speed_unit value =
  let (x1,y1) = from_unit in
  let (x2,y2) = to_unit in
  let value = convert_dist(x1,val_) x2 in
  let (_,value) = value in 
  let value = convert_time(y2, value) y1 in
  let (_,value) = value in 
  (to_unit, value)
;;

let add_speed (a : speed_unit value) ((b_unit, b_val) : speed_unit value) : speed_unit value = 
  let (x1,y1) = a in
  let speed1 = convert_speed (x1,y1) b_unit in
  let (_,speed1) = speed1 in 
  let speed = speed1 +. b_val in
  (b_unit,speed)
      
;;

let dist_traveled time ((speed_unit, speed_val) : speed_unit value) : dist_unit value = 
  let (u,t) = time in
  let (di,ti) = speed_unit in
  let ti = convert_time(u,t) ti in
  let (_,t) = ti in
  let dist = t *. speed_val in 
  (di,dist)
;;

(* Section 3 : recursive data types/induction *)

let passes_da_vinci_tests : (tree * bool) list = [
  Branch (5., [ Branch (3., [Leaf; Leaf; Leaf]); Leaf; Branch (4., []) ]), true;
  Leaf, true;
  Branch(6., [Branch(5., [Leaf;Leaf;Leaf]); Leaf ; Branch(5.,[Leaf])]), false;
  Branch (5., [ Branch (3., [Branch(2.,[Leaf]); Branch(2.,[Leaf]); Branch(2.,[Leaf])]); Leaf; Branch (4., []) ]), false;
  Branch(4.,[Leaf]),true
];;
let rec passes_da_vinci t =
  let rec sum_squares list acc = match list with
    | [] -> acc
    | x::xs -> if x = Leaf then acc+.0. 
        else let Branch (value,_) = x in
          sum_squares xs (acc +. value*.value)
  in
  match t with 
  | Leaf -> true
  | Branch (x,y) -> 
      (*let (value,restTree) = Branch in*)
      let sum = sum_squares y 0. in
      let x = x*.x in
      if (x>=sum) then
        let (x::xs) = y in
        passes_da_vinci x
      else
        false
  
  
      
  
  
;;

