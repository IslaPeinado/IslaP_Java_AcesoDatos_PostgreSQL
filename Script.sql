create schema beautyShop;

create table marca(
idMarca int auto_increment primary key,
nombreMarca varchar(25)
);

create table productos(
idProducto int auto_increment primary key,
nombreProducto varchar(50),
categoria varchar(25),
precio float,
idMarca_productos int
)