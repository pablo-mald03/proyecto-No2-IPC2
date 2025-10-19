import { Component } from '@angular/core';
import { HeaderAdminComponent } from '../../components/administrador-sistema/header-admin/header-admin.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin-sistema-page.component',
  imports: [HeaderAdminComponent,FooterComponent,RouterOutlet],
  templateUrl: './admin-sistema-page.component.html',
  styleUrl: './admin-sistema-page.component.scss'
})
export class AdminSistemaPageComponent {

}
