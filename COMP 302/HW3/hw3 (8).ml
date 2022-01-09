(* Question 1 *)

let solve_maze (maze : MazeGen.maze) : MazeGen.dir list = 
  let change (x,y,d) = match d with 
    | MazeGen.West -> (x-1,y,d)
    | MazeGen.North -> (x,y-1,d)
    | MazeGen.East -> (x+1,y,d)
    | MazeGen.South -> (x,y+1,d)
  in
  let rec next_state curr_s fc sc = 
    let (x,y,z) = curr_s in 
    let possibilities = MazeGen.dirs_to_check z in 
    let (a,b,c) = possibilities in
    if (x = 0 && y = 0) then
      sc []
    else 
      match  (not (MazeGen.has_wall maze (x,y,a))) with 
      | true -> next_state (change (x,y,a)) 
                  (fun () -> 
                     if (not (MazeGen.has_wall maze (x,y,b))) then 
                       next_state (change (x,y,b))  
                         (fun () -> if (not (MazeGen.has_wall maze (x,y,c))) 
                           then
                             next_state (change (x,y,c))  
                               (fun () -> fc ()) (fun r -> sc (c::r))
                           else 
                             fc ()) 
                         (fun r -> sc (b::r))
                     else if (not (MazeGen.has_wall maze (x,y,c))) then
                       next_state (change (x,y,c))  
                         (fun () -> fc ()) (fun r -> sc (c::r))
                     else 
                       fc ())
                  (fun r -> sc (a::r)) 
      | _ ->
          (if (not (MazeGen.has_wall maze (x,y,b))) then 
             next_state (change (x,y,b))  
               (fun () -> 
                  if (not (MazeGen.has_wall maze (x,y,c))) then 
                    next_state (change (x,y,c)) 
                      (fun () -> fc ()) (fun r -> sc (c::r)) 
                  else 
                    fc ()) 
               (fun r -> sc (b::r))
           else if (not (MazeGen.has_wall maze (x,y,c))) then
             next_state (change (x,y,c))  
               (fun () -> fc ())
               (fun r -> sc (c::r))
           else
             fc ())
  in
    
  let m = MazeGen.dims maze in 
  let (x,y) = m in
  let hori = x-1 in 
  let vert = y-1 in
  let curr = hori,vert,MazeGen.West in
      
   
  next_state curr (fun () -> []) (fun a -> a)
;;

(* You can run this code to manually test your solutions. *)
let test_maze width height =
  let maze = MazeGen.random width height in
  let path = solve_maze maze in
  print_string (MazeGen.string_of_maze ~path maze)
;;

(* Question 2 *)

module Range : RangeSig = struct
  open Seq

  let rec zip (seq1 : 'a seq) (seq2 : 'b seq) : ('a * 'b) seq = 
    fun () -> match (seq1 (), seq2 ()) with
      | (Nil,_) -> Nil
      | (_,Nil) -> Nil
      | (Cons (a,b),Cons (c,d)) -> (Cons ((a,c),zip b d)) 
            
  
  let rec keep_until (p : 'a -> bool) (seq : 'a seq) : 'a seq =
    fun () -> match (seq ()) with 
      | Nil -> Nil
      | Cons (a,b) -> if p a then
            Nil
          else
            Cons(a,keep_until p b)

  let to_list (seq : 'a seq) : 'a list =
    let rec transform seq list = 
      match (seq ()) with 
      | Nil -> list
      | Cons (a,b) -> transform b (list @ (a::[]))
    in
    transform seq []

  let range (start : int) (step : int) (stop : int) : int seq = 
    keep_until 
      (fun x-> if stop>0 then
          x>=stop 
        else
          x<=stop)
      (map (fun x -> (x * step)+start) nats)
      

end ;;

(* This function is just another example of the use of optional arguments. 
Removing or altering it should not change your grade, as the function Range.range
    is being tested. Our grader cannot, at the moment, handle optional arguments.
*)
let range ?(start : int = 0) ?(step : int = 1) (stop : int) : int seq =
  Range.range start step stop
;;

(* Examples of uses of optional arguments, lazy-evaluated *)
let example_ranges () = [
  range 10;
  range ~start:5 10;
  range ~step:2 20;
  range ~start:100 ~step:(~-2) 50
] ;;

(* Question 3 *)

module RationalField : (AlgField with type t = rational) = struct
  type t = rational
    
  let zero = {num = 0; den = 1}
  let one = {num =1; den = 1}
  
  let equal a b = a.den <> 0 && b.den <> 0 && a.den * b.num = a.num * b.den
                                                                        
  let add a b = {num = (a.num * b.den + b.num * a.den); 
                 den = (a.den * b.den)}                                                                      
  let mul a b = {num = (a.num  * b.num);
                 den = (a.den * b.den)}
  let neg a = {num = (0 - a.num);den = a.den}
  let inv a = {num = a.den; den = a.num}
                                   
                                   
end ;;

module BooleanField : (AlgField with type t = bool) = struct
  type t = bool
  let zero = false
  let one = true 
  let equal = (=)
  let add = (<>)
  let mul = (&&)
  let neg a = a 
  let inv a = a
end ;;

module EllipticCurves (F : AlgField) : sig
  type point = F.t * F.t 
  val easyECs : bool 
  val onCurve : F.t -> F.t -> point -> bool
end = struct 
  type point = F.t * F.t 
(* Implement easyECs and onCurve function below *)

  let easyECs = not (F.equal F.zero (F.add F.one (F.add F.one F.one))) 
                && not (F.equal F.zero (F.add F.one F.one))
  let onCurve p q (x,y) = 
    F.equal (F.mul y y) (F.add (F.add (F.mul x (F.mul x x)) (F.mul p x)) q)
                       
end

(* Elliptic curve definitions using functor *)
(* Do not remove these modules from your code: we need it for testing. *)
module Rational_EC = EllipticCurves(RationalField)
module Boolean_EC = EllipticCurves(BooleanField)
module Float_EC = EllipticCurves(FloatField)
(* 
As mentioned in prelude, you can test this in the Toplevel, just do not place it 
in your code below, or the grader will have a fit. 
                                                It has to do with weird dependencies in the grader. It's sad. 

module Complex_EC = EllipticCurves(ComplexField)*)
