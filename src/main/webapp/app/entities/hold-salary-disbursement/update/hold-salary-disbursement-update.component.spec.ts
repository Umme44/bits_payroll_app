import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HoldSalaryDisbursementFormService } from './hold-salary-disbursement-form.service';
import { HoldSalaryDisbursementService } from '../service/hold-salary-disbursement.service';
import { IHoldSalaryDisbursement } from '../hold-salary-disbursement.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployeeSalary } from 'app/entities/employee-salary/employee-salary.model';
import { EmployeeSalaryService } from 'app/entities/employee-salary/service/employee-salary.service';

import { HoldSalaryDisbursementUpdateComponent } from './hold-salary-disbursement-update.component';

describe('HoldSalaryDisbursement Management Update Component', () => {
  let comp: HoldSalaryDisbursementUpdateComponent;
  let fixture: ComponentFixture<HoldSalaryDisbursementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let holdSalaryDisbursementFormService: HoldSalaryDisbursementFormService;
  let holdSalaryDisbursementService: HoldSalaryDisbursementService;
  let userService: UserService;
  let employeeSalaryService: EmployeeSalaryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HoldSalaryDisbursementUpdateComponent],
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
      .overrideTemplate(HoldSalaryDisbursementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HoldSalaryDisbursementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    holdSalaryDisbursementFormService = TestBed.inject(HoldSalaryDisbursementFormService);
    holdSalaryDisbursementService = TestBed.inject(HoldSalaryDisbursementService);
    userService = TestBed.inject(UserService);
    employeeSalaryService = TestBed.inject(EmployeeSalaryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const holdSalaryDisbursement: IHoldSalaryDisbursement = { id: 456 };
      const user: IUser = { id: 43669 };
      holdSalaryDisbursement.user = user;

      const userCollection: IUser[] = [{ id: 87837 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ holdSalaryDisbursement });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call EmployeeSalary query and add missing value', () => {
      const holdSalaryDisbursement: IHoldSalaryDisbursement = { id: 456 };
      const employeeSalary: IEmployeeSalary = { id: 73058 };
      holdSalaryDisbursement.employeeSalary = employeeSalary;

      const employeeSalaryCollection: IEmployeeSalary[] = [{ id: 89558 }];
      jest.spyOn(employeeSalaryService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeSalaryCollection })));
      const additionalEmployeeSalaries = [employeeSalary];
      const expectedCollection: IEmployeeSalary[] = [...additionalEmployeeSalaries, ...employeeSalaryCollection];
      jest.spyOn(employeeSalaryService, 'addEmployeeSalaryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ holdSalaryDisbursement });
      comp.ngOnInit();

      expect(employeeSalaryService.query).toHaveBeenCalled();
      expect(employeeSalaryService.addEmployeeSalaryToCollectionIfMissing).toHaveBeenCalledWith(
        employeeSalaryCollection,
        ...additionalEmployeeSalaries.map(expect.objectContaining)
      );
      expect(comp.employeeSalariesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const holdSalaryDisbursement: IHoldSalaryDisbursement = { id: 456 };
      const user: IUser = { id: 60422 };
      holdSalaryDisbursement.user = user;
      const employeeSalary: IEmployeeSalary = { id: 4608 };
      holdSalaryDisbursement.employeeSalary = employeeSalary;

      activatedRoute.data = of({ holdSalaryDisbursement });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.employeeSalariesSharedCollection).toContain(employeeSalary);
      expect(comp.holdSalaryDisbursement).toEqual(holdSalaryDisbursement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHoldSalaryDisbursement>>();
      const holdSalaryDisbursement = { id: 123 };
      jest.spyOn(holdSalaryDisbursementFormService, 'getHoldSalaryDisbursement').mockReturnValue(holdSalaryDisbursement);
      jest.spyOn(holdSalaryDisbursementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holdSalaryDisbursement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: holdSalaryDisbursement }));
      saveSubject.complete();

      // THEN
      expect(holdSalaryDisbursementFormService.getHoldSalaryDisbursement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(holdSalaryDisbursementService.update).toHaveBeenCalledWith(expect.objectContaining(holdSalaryDisbursement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHoldSalaryDisbursement>>();
      const holdSalaryDisbursement = { id: 123 };
      jest.spyOn(holdSalaryDisbursementFormService, 'getHoldSalaryDisbursement').mockReturnValue({ id: null });
      jest.spyOn(holdSalaryDisbursementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holdSalaryDisbursement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: holdSalaryDisbursement }));
      saveSubject.complete();

      // THEN
      expect(holdSalaryDisbursementFormService.getHoldSalaryDisbursement).toHaveBeenCalled();
      expect(holdSalaryDisbursementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHoldSalaryDisbursement>>();
      const holdSalaryDisbursement = { id: 123 };
      jest.spyOn(holdSalaryDisbursementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ holdSalaryDisbursement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(holdSalaryDisbursementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployeeSalary', () => {
      it('Should forward to employeeSalaryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeSalaryService, 'compareEmployeeSalary');
        comp.compareEmployeeSalary(entity, entity2);
        expect(employeeSalaryService.compareEmployeeSalary).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
