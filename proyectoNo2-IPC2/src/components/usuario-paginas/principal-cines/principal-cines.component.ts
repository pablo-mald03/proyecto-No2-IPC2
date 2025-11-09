import { Component, OnInit } from '@angular/core';
import { CinesCardComponent } from "../../cines/cines-card.component/cines-card.component";
import { Cine } from '../../../models/cines/cine';
import { AnunciosCardsPaginaComponent } from "../anuncios-cards-pagina/anuncios-cards-pagina.component";
import { AnuncioPublicidadDTO } from '../../../models/anuncios/anuncio-publicidad-dto';
import { PublicidadPincipalService } from '../../../services/principal-service/publicidad-principal.service';
import { FormsModule, NgModel } from '@angular/forms';
import { CineInformacionService } from '../../../services/cine-service/cine-informacion.service';
import { CineInformacionDTO } from '../../../models/cines/cine-informacion-dto';
import { CantidadRegistrosDTO } from '../../../models/usuarios/cantidad-registros-dto';

@Component({
  selector: 'app-principal-cines.component',
  imports: [CinesCardComponent, AnunciosCardsPaginaComponent, FormsModule],
  templateUrl: './principal-cines.component.html',
  styleUrl: './principal-cines.component.scss'
})
export class PrincipalCinesComponent implements OnInit {


  anunciosDerecha: AnuncioPublicidadDTO[] = [];
  anunciosIzquierda: AnuncioPublicidadDTO[] = [];



  constructor(
    private publicidadService: PublicidadPincipalService,
    private cineInformacionServic: CineInformacionService,
  ) { }

  //Atributos que permiten manejar las busquedas filtradas de cines
  terminoBusqueda: string = '';
  cinesFiltrados: CineInformacionDTO[] = [];


  cinesMostrados: CineInformacionDTO[] = [];



  //Apartado de atributos que sirven para cargar dinamicamente los atributos
  indiceActual = 0;
  cantidadPorCarga = 3;
  totalReportes = 0;
  todosCargados = false;


  //Metodo que permite cargar los datos para poder motrar en la pagina
  ngOnInit(): void {

    this.cargarAnunciosAleatorios();

    this.cinesFiltrados = this.cinesMostrados;

    this.cargarAnunciosRegistrados();
  }

  //Metodo que sirve para cargar la cantidad de cines necesarios
  cargarAnunciosRegistrados() {

    this.cineInformacionServic.cantidadRegistros().subscribe({
      next: (cantidadDTO: CantidadRegistrosDTO) => {
        this.totalReportes = cantidadDTO.cantidad;
        this.indiceActual = 0;
        this.cinesMostrados = [];
        this.todosCargados = false;

        this.cargarMasRegistros();
      },
      error: (error: any) => {

        //this.mostrarError(error);

      }
    });

  }


  //Metodo que sirve para ir cargando mas cines dinamicamente
  cargarMasCines(): void {

    if (this.todosCargados || this.cinesMostrados.length === 0) {
      return;
    }

    this.cargarMasRegistros();
  }


  //Carga dinamicamente la cantidad establecida de anuncios para no saturar la web
  cargarMasRegistros(): void {

    this.cineInformacionServic.listadoRegistrosPrincipal(this.cantidadPorCarga, this.indiceActual).subscribe({
      next: (response: CineInformacionDTO[]) => {

        if (!response || response.length === 0) {
          this.todosCargados = true;
          return;
        }

        this.cinesMostrados.push(...response);
        this.indiceActual += response.length;

        if (this.indiceActual >= this.totalReportes) {
          this.todosCargados = true;
        }


      },
      error: (error: any) => {

        // this.mostrarError(error);

      }
    });
  }


  //Metodo que sirve para poder buscar a los cines 
  buscarCines(): void {
    const termino = this.terminoBusqueda.toLowerCase().trim();

    if (!termino) {
      this.cinesFiltrados = [...this.cinesMostrados]; 
      return;
    }

    this.cinesFiltrados = this.cinesMostrados.filter(cine =>
      cine.nombre.toLowerCase().includes(termino)
    );
  }

  //Método para generar número random entre min y max 
  private generarNumeroAleatorio(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

  // Método para mezclar aleatoriamente un arreglo
  private shuffleArray<T>(array: T[]): T[] {
    return array
      .map(item => ({ item, sort: Math.random() }))
      .sort((a, b) => a.sort - b.sort)
      .map(({ item }) => item);
  }

  // Cargar anuncios aleatorios desde backend
  private cargarAnunciosAleatorios(): void {
    const cantidad = this.generarNumeroAleatorio(3, 6);

    this.publicidadService.listadoPublicidad(cantidad).subscribe({
      next: (anuncios: AnuncioPublicidadDTO[]) => {
        const anunciosBarajados = this.shuffleArray(anuncios);

        // Dividimos aleatoriamente en izquierda y derecha
        anunciosBarajados.forEach((anuncio, index) => {
          if (Math.random() < 0.5) {
            this.anunciosIzquierda.push(anuncio);
          } else {
            this.anunciosDerecha.push(anuncio);
          }
        });

      },
      error: (err) => {
        console.error('Error al obtener anuncios aleatorios:', err);
      }
    });
  }



}
