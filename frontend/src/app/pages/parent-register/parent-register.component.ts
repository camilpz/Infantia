import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-parent-register',
  imports: [],
  templateUrl: './parent-register.component.html',
  styleUrl: './parent-register.component.css'
})
export class ParentRegisterComponent {
  constructor(private router: Router) {
  }

  navigate(path: string) {
    this.router.navigate([path]);
  }
}
