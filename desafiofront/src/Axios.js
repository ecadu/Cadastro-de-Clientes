import axios from "axios";

const api = axios.create({
  baseURL: "http://backend:8080", 
});

export default api;
