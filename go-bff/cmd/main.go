package main

import (
	"io"
	"io/ioutil"
	"net/http"
)

func hello(w http.ResponseWriter, r *http.Request) {
	resp := make(chan string)

	go getPersons(resp)

	io.WriteString(w, <-resp)
}

func getPersons(data chan string) error {
	resp, err := http.Get("http://192.168.3.9:8090/persons")
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {

		return err
	}

	data<- string(body)

	return nil
}

func main() {
	http.HandleFunc("/persons", hello)
	http.ListenAndServe(":8000", nil)
}
