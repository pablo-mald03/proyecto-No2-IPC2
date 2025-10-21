import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AnunciosCompradosAnuncianteCardsComponent } from "../anuncios-comprados-anunciante-cards/anuncios-comprados-anunciante-cards.component";
import { Anuncio } from '../../../models/anuncios/anuncio';

@Component({
  selector: 'app-anuncios-comprados',
  imports: [AnunciosCompradosAnuncianteCardsComponent],
  templateUrl: './anuncios-comprados.component.html',
  styleUrl: './anuncios-comprados.component.scss'
})
export class AnunciosCompradosComponent implements OnInit {

  idUsuario!: string;

  //Arreglos que se van a llenar en base a los anuncios que posea el usuario
  anuncios: Anuncio[] = [];
  anunciosMostrados: Anuncio[] = [];

  indiceActual = 0;
  cantidadPorCarga = 2;
  todosCargados = false;


  constructor(
    private router: ActivatedRoute
    //Pendiente instanciar al service

  ) {

  }

  //Metodo que permite tomar el parametro enviado
  ngOnInit(): void {

    this.idUsuario = this.router.snapshot.params['id'];


    this.anunciosMostrados = [

      {
        codigo: 'ANU-003',
        estado: true,
        nombre: 'Cine Clásico',
        caducacion: true,
        fechaExpiracion: new Date('2025-12-10'),
        fechaCompra: new Date('2025-10-18'),
        url: 'https://www.youtube.com/embed/tgbNymZ7vqY?autoplay=1&mute=1&loop=1&playlist=tgbNymZ7vqY',
        texto: 'Revive los mejores clásicos del cine.',
        foto: '',
        codigoTipo: 3,
        idUsuario: 'USR-003',
      },
      {
        codigo: 'ANU-002',
        estado: false,
        nombre: 'Nuevo Estreno: El Viaje del Tiempo',
        caducacion: true,
        fechaExpiracion: new Date('2025-12-01'),
        fechaCompra: new Date('2025-09-28'),
        url: 'imgs-app/bussiness-cinemaimg.png',
        texto: 'Descubre la nueva película de ciencia ficción que todos están esperando.',
        foto: '',
        codigoTipo: 3,
        idUsuario: 'USR-002',
      }
    ];

    if (this.anunciosMostrados.length === 0) {
      this.todosCargados = true;
    }


    //PENDIENTE LLAMAR AL SERVICE
    /* this.eventsService.getEventByCode(this.eventCode).subscribe({
      next: (eventToUpdate) => {
        this.eventToUpdate = eventToUpdate;
        this.exists = true;
      },
      error: (error: any) => {
        console.log(error);
      }
    });
*/

    console.log(this.idUsuario);

  }

  //Carga dinamicamente la cantidad establecida de anuncios para no saturar la web
  cargarMasAnuncios(): void {
    const siguienteBloque = this.anuncios.slice(
      this.indiceActual,
      this.indiceActual + this.cantidadPorCarga
    );

    this.anunciosMostrados.push(...siguienteBloque);
    this.indiceActual += this.cantidadPorCarga;

    if (this.indiceActual >= this.anuncios.length) {
      this.todosCargados = true;
    }
  }


}
