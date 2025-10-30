import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../../components/header/header.component";
import { FooterComponent } from "../../components/footer/footer.component";
import { RouterOutlet } from '@angular/router';
import { Cine } from '../../models/cines/cine';
import { CinesCardComponent } from '../../components/cines/cines-card.component/cines-card.component';

@Component({
  selector: 'app-principal-usuario-page',
  imports: [HeaderComponent, FooterComponent, RouterOutlet],
  templateUrl: './principal-usuario-page.component.html',
  styleUrl: './principal-usuario-page.component.scss'
})
export class PrincipalUsuarioPageComponent implements OnInit {




  ngOnInit(): void {
    
  }



}
