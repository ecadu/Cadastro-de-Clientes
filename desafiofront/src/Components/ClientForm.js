import * as React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import * as yup from "yup";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import api from "../Axios";
import Select from "@mui/material/Select";
import FormControl from "@mui/material/FormControl";
import SaveModal from "./SaveModal";
export default function ClientForm() {
    const [colors, setColors] = React.useState([]);
    const [openModal, setOpenModal] = React.useState(false);
    const [modalData, setModalData] = React.useState(null);
    const [formData, setFormData] = React.useState({
        clientName: "",
        cpf: "",
        colorId: "",
        email: "",
    });
    const [errors, setErrors] = React.useState({});
    React.useEffect(() => {
        const cachedColors = localStorage.getItem("colors");
        if (cachedColors) {
            setColors(JSON.parse(cachedColors));
            return;
        }
        api.get("/clients/colors")
            .then((response) => {
                setColors(response.data);
                localStorage.setItem("colors", JSON.stringify(response.data));
            })
            .catch((error) => console.error("Erro ao buscar cores:", error));
    }, []);
    const formatCPF = (value) => {
        return value
            .replace(/\D/g, "")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    };
    const schema = yup.object().shape({
        clientName: yup.string().trim().required("O nome é obrigatório"),
        cpf: yup
            .string()
            .trim()
            .required("O CPF é obrigatório")
            .matches(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF inválido"),
        email: yup.string().trim().required("O e-mail é obrigatório").email("E-mail inválido"),
        colorId: yup.string().trim().required("A cor favorita é obrigatória"),
    });
    const validarDados = async (data) => {
        try {
            await schema.validate(data, { abortEarly: false });
            return { valido: true, erros: {} };
        } catch (err) {
            let erros = {};
            err.inner.forEach((error) => {
                erros[error.path] = error.message;
            });
            return { valido: false, erros };
        }
    };
    const handleChange = async (event) => {
        let { name, value } = event.target;
        if (name === "cpf") {
            value = formatCPF(value);
        }
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
        try {
            await schema.validateAt(name, { [name]: value });
            setErrors((prevErrors) => ({
                ...prevErrors,
                [name]: undefined,
            }));
        } catch (err) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                [name]: err.message,
            }));
        }
    };
    const handleSaveClient = async (event) => {
        event.preventDefault();
        const { valido, erros } = await validarDados(formData);
        if (!valido) {
            setErrors(erros);
            return;
        }
        api.post("/clients/CreateOrUpdateClient", formData)
            .then((response) => {
                setModalData({
                    Title: "Sucesso",
                    Mensage: "Cadastro efetuado com sucesso",
                    TextButton:"Fechar"
                });
                setOpenModal(true);
                setFormData({
                    clientName: "",
                    cpf: "",
                    colorId: "",
                    email: "",
                });
            })
            .catch((error) => {
                const errorMessage = error.response?.data || "Erro inesperado";
                setFormData({
                    clientName: "",
                    cpf: "",
                    colorId: "",
                    email: "",
                });
                setModalData({
                    Title: "Erro",
                    Mensage: errorMessage,
                    TextButton:"tentar novamente"
                });
                setOpenModal(true);
            });
    };
    const handleChangeSelect = (event) => {
        setFormData((prevData) => ({
            ...prevData,
            colorId: event.target.value,
        }));
    };
    return (
        <Container maxWidth="sm">
        <Box sx={{ 
            mt: 4, 
            p: 3, 
            boxShadow: 3, 
            borderRadius: 2, 
            backgroundColor: "#f5f5f5" 
        }}>
            <Typography variant="h5" gutterBottom sx={{ color: "#333", fontWeight: "bold" }}>
                Cadastrar Cliente
            </Typography>
            <TextField
                required
                margin="dense"
                name="clientName"
                label="Nome"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.clientName}
                onChange={handleChange}
                error={!!errors.clientName}
                helperText={errors.clientName || ""}
            />
            <TextField
                required
                margin="dense"
                name="email"
                label="Email"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.email}
                onChange={handleChange}
                error={!!errors.email}
                helperText={errors.email || ""}
            />
            <TextField
                required
                margin="dense"
                name="cpf"
                label="CPF"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.cpf}
                onChange={handleChange}
                error={!!errors.cpf}
                helperText={errors.cpf || ""}
            />
            <FormControl fullWidth margin="dense" error={!!errors.colorId}>
                <InputLabel id="color-select-label">Cor Favorita</InputLabel>
                <Select
                    labelId="color-select-label"
                    id="color-select"
                    value={formData.colorId}
                    label="Cor Favorita"
                    onChange={handleChangeSelect}
                >
                    {colors.map((color) => (
                        <MenuItem key={color.id} value={color.id}>
                            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                                <Box
                                    sx={{
                                        width: 16,
                                        height: 16,
                                        backgroundColor: color.hexCode,
                                        borderRadius: "50%",
                                    }}
                                />
                                {color.colorName}
                            </Box>
                        </MenuItem>
                    ))}
                </Select>
                {errors.colorId && (
                    <Typography color="error" variant="body2">
                        {errors.colorId}
                    </Typography>
                )}
            </FormControl>
            <Button sx={{ mt: 2 }} fullWidth variant="contained" color="primary" onClick={handleSaveClient}>
                Cadastrar
            </Button>
        </Box>
        <SaveModal open={openModal} onClose={() => setOpenModal(false)} data={modalData} />
    </Container>
    );
}
