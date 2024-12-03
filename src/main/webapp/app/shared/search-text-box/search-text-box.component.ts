import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable, OperatorFunction } from 'rxjs';

import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';
import {CustomValidator} from "../../validators/custom-validator";

@Component({
  selector: 'jhi-search-text-box',
  templateUrl: './search-text-box.component.html',
  styleUrls: ['./search-text-box.component.scss'],
})
export class SearchTextBoxComponent {
  @Input()
  placeHolderText = 'Search';

  @Input()
  searchBoxSize = '18rem';

  @Input()
  inputMaxlength = 50;

  @Input()
  suggestionList: string[] = [];

  @Output() searchEvent = new EventEmitter<string>();

  searchTextFormControl = new FormControl('', CustomValidator.naturalTextValidator());

  previousTextValue!: string;

  // nbg typeahead
  search: OperatorFunction<string, readonly string[]> = (text$: Observable<string>) => {
    return text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => {
        if(this.searchTextFormControl.errors?.pattern != true) {
          //this.suggestionList.unshift(term);
          this.suggestionList[0] = term;
          return term.length < 2 ? [] : this.suggestionList.filter(item => item.toLowerCase().includes(term.toLowerCase())).slice(0, 10);
        }
        else return [];
      })
    );
  };

  // escape button, clear search box
  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    if (this.searchTextFormControl.value.toString().length !== 0) {
      this.searchTextFormControl.setValue('');
      this.searchEvent.emit('');
    }
  }

  searchTextOnKeyBoardEnter(value: any): void {
    // do not re-search, if keyword is same
    const searchText = this.searchTextFormControl.value.toString().trim();
    if (value.target.value.trim().length !== 0 && this.previousTextValue !== searchText) {
      this.searchEvent.emit(value.target.value.trim());
      this.previousTextValue = searchText;
    }
  }

  selectedItem(itemObject: any): void {
    this.searchEvent.emit(itemObject.item);
  }

  searchOnBtnClick(): void {
    // do not re-search, if keyword is same
    const searchText = this.searchTextFormControl.value.toString().trim();
    if (searchText.length !== 0 && this.previousTextValue !== searchText) {
      this.searchEvent.emit(searchText);
      this.previousTextValue = searchText;
    }
  }

  onInput($event: any): void {
    if ($event.target.value.length === 0) {
      this.searchEvent.emit('');
    }
  }
}
