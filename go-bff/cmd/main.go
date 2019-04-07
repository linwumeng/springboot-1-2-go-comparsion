package main

import (
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
)

func hello(w http.ResponseWriter, r *http.Request) {
	resp, err := http.Get("http://192.168.3.9:8090/persons")
	if err != nil {
		fmt.Println(err.Error())

		return
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		fmt.Println(err.Error())

		return
	}

	io.WriteString(w, string(body))
}

func main() {
	http.HandleFunc("/persons", hello)
	http.ListenAndServe(":8000", nil)
}
