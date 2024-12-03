import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PfLoanRepaymentFormService } from './pf-loan-repayment-form.service';
import { PfLoanRepaymentService } from '../service/pf-loan-repayment.service';
import { IPfLoanRepayment } from '../pf-loan-repayment.model';
import { IPfLoan } from 'app/entities/pf-loan/pf-loan.model';
import { PfLoanService } from 'app/entities/pf-loan/service/pf-loan.service';

import { PfLoanRepaymentUpdateComponent } from './pf-loan-repayment-update.component';

describe('PfLoanRepayment Management Update Component', () => {
  let comp: PfLoanRepaymentUpdateComponent;
  let fixture: ComponentFixture<PfLoanRepaymentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pfLoanRepaymentFormService: PfLoanRepaymentFormService;
  let pfLoanRepaymentService: PfLoanRepaymentService;
  let pfLoanService: PfLoanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PfLoanRepaymentUpdateComponent],
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
      .overrideTemplate(PfLoanRepaymentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PfLoanRepaymentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pfLoanRepaymentFormService = TestBed.inject(PfLoanRepaymentFormService);
    pfLoanRepaymentService = TestBed.inject(PfLoanRepaymentService);
    pfLoanService = TestBed.inject(PfLoanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PfLoan query and add missing value', () => {
      const pfLoanRepayment: IPfLoanRepayment = { id: 456 };
      const pfLoan: IPfLoan = { id: 31252 };
      pfLoanRepayment.pfLoanId = pfLoan.id;

      const pfLoanCollection: IPfLoan[] = [{ id: 99404 }];
      jest.spyOn(pfLoanService, 'query').mockReturnValue(of(new HttpResponse({ body: pfLoanCollection })));
      const additionalPfLoans = [pfLoan];
      const expectedCollection: IPfLoan[] = [...additionalPfLoans, ...pfLoanCollection];
      jest.spyOn(pfLoanService, 'addPfLoanToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pfLoanRepayment });
      comp.ngOnInit();

      expect(pfLoanService.query).toHaveBeenCalled();
      expect(pfLoanService.addPfLoanToCollectionIfMissing).toHaveBeenCalledWith(
        pfLoanCollection,
        ...additionalPfLoans.map(expect.objectContaining)
      );
    });

    it('Should update editForm', () => {
      const pfLoanRepayment: IPfLoanRepayment = { id: 456 };
      const pfLoan: IPfLoan = { id: 876 };
      pfLoanRepayment.pfLoanId = pfLoan.id;

      activatedRoute.data = of({ pfLoanRepayment });
      comp.ngOnInit();

      expect(comp.pfLoanRepayment).toEqual(pfLoanRepayment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfLoanRepayment>>();
      const pfLoanRepayment = { id: 123 };
      jest.spyOn(pfLoanRepaymentFormService, 'getPfLoanRepayment').mockReturnValue(pfLoanRepayment);
      jest.spyOn(pfLoanRepaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfLoanRepayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfLoanRepayment }));
      saveSubject.complete();

      // THEN
      expect(pfLoanRepaymentFormService.getPfLoanRepayment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pfLoanRepaymentService.update).toHaveBeenCalledWith(expect.objectContaining(pfLoanRepayment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfLoanRepayment>>();
      const pfLoanRepayment = { id: 123 };
      jest.spyOn(pfLoanRepaymentFormService, 'getPfLoanRepayment').mockReturnValue({ id: null });
      jest.spyOn(pfLoanRepaymentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfLoanRepayment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pfLoanRepayment }));
      saveSubject.complete();

      // THEN
      expect(pfLoanRepaymentFormService.getPfLoanRepayment).toHaveBeenCalled();
      expect(pfLoanRepaymentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPfLoanRepayment>>();
      const pfLoanRepayment = { id: 123 };
      jest.spyOn(pfLoanRepaymentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pfLoanRepayment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pfLoanRepaymentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePfLoan', () => {
      it('Should forward to pfLoanService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pfLoanService, 'comparePfLoan');
        comp.comparePfLoan(entity, entity2);
        expect(pfLoanService.comparePfLoan).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
