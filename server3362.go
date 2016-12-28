package main

import "net"
import "fmt"
import "bufio"
// import "io/ioutil"
import "strings" // only needed below for sample processing
import "github.com/go-vgo/robotgo"
import "strconv"
import "time"

func main() {
	for kk := 1; kk <= 7; kk++ {
		for ll := 1; ll <= 10; ll++ {
			kkk := kk + 1
			lll := ll

			if ll <= 3 || ll >= 8 && ll <= 10 || kk == ll || kkk == lll {
				fmt.Print("-")
				time.Sleep(100 * time.Millisecond)

			} else {
				fmt.Print(" ")
				time.Sleep(100 * time.Millisecond)
			}
		}
		fmt.Println("")
	}
  fmt.Println("\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x50\x72\x6f\x67\x72\x61\x6d\x6d\x65\x64\x20\x42\x79\x20\x4e\x41\x47\x41\x52\x41\x4a\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20")
  fmt.Println("Launching server...")

  // listen on all interfaces
  ln, err := net.Listen("tcp", ":3362")
  if err != nil {
	// handle error
    fmt.Println("error occured",err)
}else{
  fmt.Println("server is alive")
}

  // accept connection on port
  conn, errr := ln.Accept()
  if errr !=nil{
    fmt.Println(errr)
  }
  defer conn.Close()

  // run loop forever (or until ctrl-c)
  for {
    // will listen for message to process ending in newline (\n)
    message, e:= bufio.NewReader(conn).ReadString('\n')
     if e !=nil{
        fmt.Println("error",err)
      }
    // output message received
    fmt.Print(string(message))
    // sample process for string received
    newmessage := strings.ToUpper(string(message))
    str:=string(message)
    fmt.Println(str)
    if strings.Contains(str,","){
      pos:=strings.Split(str,",") 
      dx,err:=strconv.ParseFloat(pos[0],64)
      if err !=nil{
        fmt.Println("error",err)
      }
      data:=strings.Split(pos[1],"\n")
      dy,error:=strconv.ParseFloat(data[0],64)
       if error !=nil{
        fmt.Println("error",error)
      }
    x,y:=robotgo.GetMousePos() 
    x1:=float64(x)
    y1:=float64(y) 
    robotgo.MoveMouse(int(x1+dx),int(y1+dy))
    fmt.Println(x1,dx,int(y1+dy))
    }else  if strings.Contains(str,"space"){
    robotgo.KeyTap("space")
    fmt.Println("space")
  }else  if strings.Contains(str,"tab"){
    robotgo.KeyTap("tab")
       fmt.Println("tab")
  }else  if strings.Contains(str,"left"){
    robotgo.MouseClick("left",true)
       fmt.Println("left")
  }else  if strings.Contains(str,"right"){
    robotgo.MouseClick("right",false)
       fmt.Println("right"+newmessage)
  }
    // send new string back to client
    conn.Write([]byte("connected "+ "\n"))
  }
}