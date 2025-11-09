import { Component, OnInit } from '@angular/core';
import { CinesCardComponent } from "../../cines/cines-card.component/cines-card.component";
import { Cine } from '../../../models/cines/cine';
import { AnunciosCardsPaginaComponent } from "../anuncios-cards-pagina/anuncios-cards-pagina.component";
import { AnuncioPublicidadDTO } from '../../../models/anuncios/anuncio-publicidad-dto';
import { PublicidadPincipalService } from '../../../services/principal-service/publicidad-principal.service';

@Component({
  selector: 'app-principal-cines.component',
  imports: [CinesCardComponent, AnunciosCardsPaginaComponent],
  templateUrl: './principal-cines.component.html',
  styleUrl: './principal-cines.component.scss'
})
export class PrincipalCinesComponent implements OnInit {


  anunciosDerecha: AnuncioPublicidadDTO[] = [];
  anunciosIzquierda: AnuncioPublicidadDTO[] = [];



  constructor(
    private publicidadService: PublicidadPincipalService,
  ) { }

  //Pendiente jugar con las peticiones hacia el back
  showAd1 = true;
  showAd2 = false;
  showAd3 = false;
  showAd4 = true;
  showAd5 = false;
  showAd6 = true;



  cines: Cine[] = [];
  cinesMostrados: Cine[] = [];
  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;


  ngOnInit(): void {

    this.cargarAnunciosAleatorios();

    this.cines = [
      {
        codigo: 'CIN001',
        nombre: 'Cinemark Oakland',
        estadoAnuncio: true,
        montoOcultacion: 1500,
        fechaCreacion: '2024-02-01T00:00:00',
        descripcion: 'Uno de los cines más modernos de la ciudad con sonido Dolby Atmos.',
        ubicacion: 'Zona 10, Ciudad de Guatemala'
      },
      {
        codigo: 'CIN002',
        nombre: 'Cine Capitol',
        estadoAnuncio: false,
        montoOcultacion: 800,
        fechaCreacion: '2023-07-15T00:00:00',
        descripcion: 'Histórico cine restaurado con encanto clásico y proyecciones independientes.',
        ubicacion: 'Centro Histórico, Ciudad de Guatemala'
      },
      {
        codigo: 'CIN003',
        nombre: 'Cine Pradera Xela',
        estadoAnuncio: true,
        montoOcultacion: 1200,
        fechaCreacion: '2022-12-10T00:00:00',
        descripcion: 'Sala premium con butacas reclinables y pantalla gigante.',
        ubicacion: 'Quetzaltenango, Guatemala'
      },
      {
        codigo: 'CIN004',
        nombre: 'Cine Portales',
        estadoAnuncio: true,
        montoOcultacion: 1300,
        fechaCreacion: '2022-10-01T00:00:00',
        descripcion: 'Un cine con amplia oferta gastronómica y salas confortables.',
        ubicacion: 'Zona 17, Ciudad de Guatemala'
      }
    ];

    this.cargarMasCines();
  }

  //Metodo que sirve para ir cargando mas cines dinamicamente
  cargarMasCines(): void {
    const siguienteBloque = this.cines.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.cinesMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.cines.length) {
      this.todosCargados = true;
    }
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
    console.log('Cantidad aleatoria solicitada:', cantidad);

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

        console.log('Anuncios izquierda:', this.anunciosIzquierda);
        console.log('Anuncios derecha:', this.anunciosDerecha);
      },
      error: (err) => {
        console.error('Error al obtener anuncios aleatorios:', err);
      }
    });
  }



}
