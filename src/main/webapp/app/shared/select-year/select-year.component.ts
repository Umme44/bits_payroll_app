import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'jhi-select-year',
  templateUrl: './select-year.component.html',
  styleUrls: ['./select-year.component.scss'],
})
export class SelectYearComponent implements OnInit {
  @Input()
  startFromCurrentYear = true;

  @Input()
  year!: number;

  @Input()
  countOfLastYears = 5;

  @Input()
  ascYearOrder = true;

  @Input()
  suggestionList: string[] = [];

  @Input()
  showWithPlaceHolder = true;

  @Output() selectYearEventEmitter = new EventEmitter<number>();

  selectYearFormControl = new FormControl(0);

  years: number[] = [];

  ngOnInit(): void {
    let currentYear = this.year;
    if (this.startFromCurrentYear) {
      currentYear = new Date().getFullYear();
    }
    for (let i = 0; i < this.countOfLastYears; i++) {
      this.years.push(currentYear - i);
    }
    if (this.showWithPlaceHolder) {
      this.selectYearFormControl.setValue(null);
    } else {
      this.selectYearFormControl.setValue(this.years[0]);
    }
  }

  // ngOnChanges(changes: SimpleChanges): void {
  //   let currentYear = this.year;
  //   if (this.startFromCurrentYear) {
  //     currentYear = new Date().getFullYear();
  //   }
  //   for (let i = 0; i < this.countOfLastYears; i++) {
  //     this.years.push(currentYear - i);
  //   }
  //   if (this.showWithPlaceHolder) {
  //     this.selectYearFormControl.setValue(0);
  //   } else {
  //     this.selectYearFormControl.setValue(this.years[0]);
  //   }
  // }

  // escape button, clear search box
  /*@HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    if (this.searchTextFormControl.value.toString().length !== 0) {
      this.searchTextFormControl.setValue('');
      this.searchEvent.emit('');
    }
  }*/
  onChangeYear($event: any): void {
    this.selectYearEventEmitter.emit(Number($event.target.value));
    // eslint-disable-next-line no-console
    console.log($event);
  }
}
