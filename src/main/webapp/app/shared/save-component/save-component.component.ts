import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-save-component',
  templateUrl: './save-component.component.html',
  styleUrls: ['./save-component.component.scss'],
})
export class SaveComponentComponent implements OnInit {
  @Input()
  text!: string;
  headerText!: string;
  leftButton!: string;
  rightButton!: string;

  @Output() passEntry: EventEmitter<boolean> = new EventEmitter();

  constructor(private activeModal: NgbActiveModal) {}

  ngOnInit(): void {}

  cancel(): void {
    this.passEntry.emit(false);
    this.activeModal.dismiss();
  }

  confirm(): void {
    this.passEntry.emit(true);
    this.activeModal.dismiss();
  }
}
