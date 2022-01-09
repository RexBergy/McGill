
(* Question 1 *)

let rec find_map (f : 'a -> 'b option) (l : 'a list) : 'b option =
  match l with
  | [] -> None
  | x::xs -> let v = f x in
      match v with 
      | Some x -> v
      | None -> find_map f xs
;;


let partition (p : 'a -> bool) (l : 'a list) : ('a list * 'a list) = 
  (*let f = fun  x -> x  in*) 
  (*let l1 = [] in
   let l2 = [] in
   List.fold_left (fun (m1,m2) x -> if p x then (x::m1,m2)
                    else (m1,x::m2) ) (l1,l2) l *)
  let l1 = [] in 
  let l2 = [] in
  List.fold_right (fun  x (l1,l2) ->
      if p x then (x::l1,l2)
      else (l1,x::l2)) l (l1,l2)
    
           
             
;;

(* Question 2 *)

let make_manager (masterpass : masterpass) : pass_manager = 
  let ref_list : (address * password) list ref = ref [] in
  let master_ref = ref masterpass in 
  let count = ref 0 in 
  let wrongPassCount = ref 0 in
  let check master = master = !master_ref in
  let save masterpass address password = 
    if !wrongPassCount > 3 then
      raise AccountLocked
    else if check masterpass then
      (ref_list := (address,(encrypt masterpass password))::(!ref_list);
       wrongPassCount := 0;
       (count := !count +1))
    else
      (wrongPassCount := !wrongPassCount +1;
       raise WrongPassword)
  in
  let get_force masterpass address = 
    find_map (fun (x,y) -> if x = address then 
                 Some  (decrypt masterpass y)
               else
                 None) !ref_list
  in
  let get masterpass address = 
    if !wrongPassCount > 3 then
      raise AccountLocked
    else  if check masterpass then
      (count := !count + 1; 
       wrongPassCount := 0;
       get_force masterpass address)
    else
      (wrongPassCount := !wrongPassCount +1;
       raise WrongPassword)
  in
  let update_master currmaster newmaster = 
    let old_ref = ref !ref_list in
    ref_list := [];
    let rec update_passwords oldmaster newmaster lst = 
      match lst with 
      | [] -> ()
      | x::xs -> let (a,p) = x in 
          ref_list := (a,encrypt newmaster (decrypt oldmaster p))::!ref_list;
          update_passwords oldmaster newmaster xs 
    in 
    if check currmaster then 
      (update_passwords currmaster newmaster !old_ref; 
       wrongPassCount := 0;
       master_ref := newmaster) 
    else
      (wrongPassCount := !wrongPassCount +1;
       raise WrongPassword)
  in
  let count_ops masterpass =
    if !wrongPassCount > 3 then
      raise AccountLocked
    else if check masterpass then
      (count := !count + 1;
       wrongPassCount := 0;
       !count)
    else
      (wrongPassCount := !wrongPassCount +1;
       raise WrongPassword)
  in
  
  {save; get_force; get; update_master; count_ops}
          
  
    
;;

(* Question 3 *)

(* Counting values at same time *)
let catalan_count (n : int) : (int * int) = 
  let count_rec_calls = ref 0 in
  let rec catalan n = 
    count_rec_calls := !count_rec_calls + 1;
    if n = 0 then 1
    else 
      let rec aux i n acc = 
        if i > n then acc
        else 
          aux (i + 1) n (acc + (catalan i) * (catalan (n - i)))
      in
      aux 0 (n-1) 0
  in
  let g = catalan n in
  g, !count_rec_calls
;;

(* Memoization function *)
let memoize (f : ('a -> 'b) -> 'a -> 'b) (stats : stats) : 'a -> 'b =
  let hash = Hashtbl.create 1000 in 
  let rec f' x = 
    
    if Hashtbl.mem hash x then 
      (stats.lkp := !(stats.lkp) + 1; 
       Hashtbl.find hash x)
    else
      (Hashtbl.add hash x (f f' x);
       stats.entries := !(stats.entries) + 1;
       Hashtbl.find hash x)
  in
  f'
;;

(* Version of catalan that can be memoized *)
let memo_cat (recf : int -> int) (n : int) : int = 
  if n=0 then
    1
  else 
    let rec aux i n acc =
      if i > n then acc
      else aux (i + 1) n  (acc + recf i * recf (n - i))
    in 
    aux 0 (n-1) 0
;;

let catalan_m (n : int) : int * stats = 
  let entries = ref 0 in
  let lkp = ref 0 in
  
  let statistics = {entries ;lkp} in
  let v = memoize memo_cat statistics n in
  v, statistics
;;

