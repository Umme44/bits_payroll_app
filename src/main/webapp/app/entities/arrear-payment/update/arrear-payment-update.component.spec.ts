import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ArrearPaymentFormService } from './arrear-payment-form.service';
import { ArrearPaymentService } from '../service/arrear-payment.service';
import { IArrearPayment } from '../arrear-payment.model';
import { IArrearSalaryItem } from 'app/entities/arrear-salary-item/arrear-salary-item.model';
import { ArrearSalaryItemService } from 'app/entities/arrear-salary-item/service/arrear-salary-item.service';

import { ArrearPaymentUpdateComponent } from './arrear-payment-update.component';

describe('ArrearPayment Management Update Component', () => {
  let comp: ArrearPaymentUpdateComponent;
  let fixture: ComponentFixture<ArrearPaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let arrearPaymentFormService: ArrearPaymentFormService;
  let arrearPaymentService: ArrearPaymentService;
  let arrearSalaryItemService: ArrearSalaryItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ArrearPaymentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ArrearPaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArrearPaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    arrearPaymentFormService = TestBed.inject(ArrearPaymentFormService);
    arrearPaymentService = TestBed.inject(ArrearPaymentService);
    arrearSalaryItemService = TestBed.inject(ArrearSalaryItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ArrearSalaryItem query and add missing value', () => {
      const arrearPayment: IArrearPayment = { id: 456 };
      const arrearSalaryItem: IArrearSalaryItem = { id: 70881 };
      arrearPayment.arrearSalaryItem = arrearSalaryItem;

      const arrearSalaryItemCollection: IArrearSalaryItem[] = [{ id: 6942 }];
      jest.spyOn(arrearSalaryItemService, 'query').mockReturnValue(of(new HttpResponse({ body: arrearSalaryItemCollection })));
      const additionalArrearSalaryItems = [arrearSalaryItem];
      const expectedCollection: IArrearSalaryItem[] = [...additionalArrearSalaryItems, ...arrearSalaryItemCollection];
      jest.spyOn(arrearSalaryItemService, 'addArrearSalaryItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ arrearPayment });
      comp.ngOnInit();

      expect(arrearSalaryItemService.query).toHaveBeenCalled();
      expect(arrearSalaryItemService.addArrearSalaryItemToCollectionIfMissing).toHaveBeenCalledWith(
        arrearSalaryItemCollection,
        ...additionalArrearSalaryItems.map(expect.objectContaining)
      );
      expect(comp.arrearSalaryItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const arrearPayment: IArrearPayment = { id: 456 };
      const arrearSalaryItem: IArrearSalaryItem = { id: 38191 };
      arrearPayment.arrearSalaryItem = arrearSalaryItem;

      activatedRoute.data = of({ arrearPayment });
      comp.ngOnInit();

      expect(comp.arrearSalaryItemsSharedCollection).toContain(arrearSalaryItem);
      expect(comp.arrearPayment).toEqual(arrearPayment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearPayment>>();
      const arrearPayment = { id: 123 };
      jest.spyOn(arrearPaymentFormService, 'getArrearPayment').mockReturnValue(arrearPayment);
      jest.spyOn(arrearPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arrearPayment }));
      saveSubject.complete();

      // THEN
      expect(arrearPaymentFormService.getArrearPayment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(arrearPaymentService.update).toHaveBeenCalledWith(expect.objectContaining(arrearPayment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearPayment>>();
      const arrearPayment = { id: 123 };
      jest.spyOn(arrearPaymentFormService, 'getArrearPayment').mockReturnValue({ id: null });
      jest.spyOn(arrearPaymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearPayment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arrearPayment }));
      saveSubject.complete();

      // THEN
      expect(arrearPaymentFormService.getArrearPayment).toHaveBeenCalled();
      expect(arrearPaymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearPayment>>();
      const arrearPayment = { id: 123 };
      jest.spyOn(arrearPaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearPayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(arrearPaymentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareArrearSalaryItem', () => {
      it('Should forward to arrearSalaryItemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(arrearSalaryItemService, 'compareArrearSalaryItem');
        comp.compareArrearSalaryItem(entity, entity2);
        expect(arrearSalaryItemService.compareArrearSalaryItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
