jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FinalSettlementService } from '../service/final-settlement.service';

import { FinalSettlementDeleteDialogComponent } from './final-settlement-delete-dialog.component';

describe('FinalSettlement Management Delete Component', () => {
  let comp: FinalSettlementDeleteDialogComponent;
  let fixture: ComponentFixture<FinalSettlementDeleteDialogComponent>;
  let service: FinalSettlementService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FinalSettlementDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(FinalSettlementDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FinalSettlementDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FinalSettlementService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
