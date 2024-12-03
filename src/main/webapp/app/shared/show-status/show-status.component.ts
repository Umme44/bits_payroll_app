import { Component, Input } from '@angular/core';
import { Status } from '../model/enumerations/status.model';

@Component({
  selector: 'jhi-show-status',
  templateUrl: './show-status.component.html',
  styleUrls: ['./show-status.component.scss'],
})
export class ShowStatusComponent {
  @Input()
  status!: Status | string;
}
