# Use a imagem oficial do Node.js
FROM node:16

# Defina o diretório de trabalho
WORKDIR /app

# Copie os arquivos de configuração e o código-fonte para o container
COPY . /app

# Instale as dependências
RUN npm install

# Crie os arquivos de build da sua aplicação frontend (React, por exemplo)
RUN npm run build

# Exponha a porta 3000 (ou qualquer porta que você esteja usando)
EXPOSE 3000

# Configure o comando para rodar a aplicação usando o Node.js
CMD ["npm", "start"]
