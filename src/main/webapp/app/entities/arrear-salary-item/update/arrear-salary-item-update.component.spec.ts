import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ArrearSalaryItemFormService } from './arrear-salary-item-form.service';
import { ArrearSalaryItemService } from '../service/arrear-salary-item.service';
import { IArrearSalaryItem } from '../arrear-salary-item.model';
import { IArrearSalaryMaster } from 'app/entities/arrear-salary-master/arrear-salary-master.model';
import { ArrearSalaryMasterService } from 'app/entities/arrear-salary-master/service/arrear-salary-master.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { ArrearSalaryItemUpdateComponent } from './arrear-salary-item-update.component';

describe('ArrearSalaryItem Management Update Component', () => {
  let comp: ArrearSalaryItemUpdateComponent;
  let fixture: ComponentFixture<ArrearSalaryItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let arrearSalaryItemFormService: ArrearSalaryItemFormService;
  let arrearSalaryItemService: ArrearSalaryItemService;
  let arrearSalaryMasterService: ArrearSalaryMasterService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ArrearSalaryItemUpdateComponent],
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
      .overrideTemplate(ArrearSalaryItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArrearSalaryItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    arrearSalaryItemFormService = TestBed.inject(ArrearSalaryItemFormService);
    arrearSalaryItemService = TestBed.inject(ArrearSalaryItemService);
    arrearSalaryMasterService = TestBed.inject(ArrearSalaryMasterService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ArrearSalaryMaster query and add missing value', () => {
      const arrearSalaryItem: IArrearSalaryItem = { id: 456 };
      const arrearSalaryMaster: IArrearSalaryMaster = { id: 14175 };
      arrearSalaryItem.arrearSalaryMaster = arrearSalaryMaster;

      const arrearSalaryMasterCollection: IArrearSalaryMaster[] = [{ id: 37027 }];
      jest.spyOn(arrearSalaryMasterService, 'query').mockReturnValue(of(new HttpResponse({ body: arrearSalaryMasterCollection })));
      const additionalArrearSalaryMasters = [arrearSalaryMaster];
      const expectedCollection: IArrearSalaryMaster[] = [...additionalArrearSalaryMasters, ...arrearSalaryMasterCollection];
      jest.spyOn(arrearSalaryMasterService, 'addArrearSalaryMasterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ arrearSalaryItem });
      comp.ngOnInit();

      expect(arrearSalaryMasterService.query).toHaveBeenCalled();
      expect(arrearSalaryMasterService.addArrearSalaryMasterToCollectionIfMissing).toHaveBeenCalledWith(
        arrearSalaryMasterCollection,
        ...additionalArrearSalaryMasters.map(expect.objectContaining)
      );
      expect(comp.arrearSalaryMastersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const arrearSalaryItem: IArrearSalaryItem = { id: 456 };
      const employee: IEmployee = { id: 63262 };
      arrearSalaryItem.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 21384 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ arrearSalaryItem });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining)
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const arrearSalaryItem: IArrearSalaryItem = { id: 456 };
      const arrearSalaryMaster: IArrearSalaryMaster = { id: 66854 };
      arrearSalaryItem.arrearSalaryMaster = arrearSalaryMaster;
      const employee: IEmployee = { id: 67784 };
      arrearSalaryItem.employee = employee;

      activatedRoute.data = of({ arrearSalaryItem });
      comp.ngOnInit();

      expect(comp.arrearSalaryMastersSharedCollection).toContain(arrearSalaryMaster);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.arrearSalaryItem).toEqual(arrearSalaryItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearSalaryItem>>();
      const arrearSalaryItem = { id: 123 };
      jest.spyOn(arrearSalaryItemFormService, 'getArrearSalaryItem').mockReturnValue(arrearSalaryItem);
      jest.spyOn(arrearSalaryItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearSalaryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arrearSalaryItem }));
      saveSubject.complete();

      // THEN
      expect(arrearSalaryItemFormService.getArrearSalaryItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(arrearSalaryItemService.update).toHaveBeenCalledWith(expect.objectContaining(arrearSalaryItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearSalaryItem>>();
      const arrearSalaryItem = { id: 123 };
      jest.spyOn(arrearSalaryItemFormService, 'getArrearSalaryItem').mockReturnValue({ id: null });
      jest.spyOn(arrearSalaryItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearSalaryItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: arrearSalaryItem }));
      saveSubject.complete();

      // THEN
      expect(arrearSalaryItemFormService.getArrearSalaryItem).toHaveBeenCalled();
      expect(arrearSalaryItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArrearSalaryItem>>();
      const arrearSalaryItem = { id: 123 };
      jest.spyOn(arrearSalaryItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ arrearSalaryItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(arrearSalaryItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareArrearSalaryMaster', () => {
      it('Should forward to arrearSalaryMasterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(arrearSalaryMasterService, 'compareArrearSalaryMaster');
        comp.compareArrearSalaryMaster(entity, entity2);
        expect(arrearSalaryMasterService.compareArrearSalaryMaster).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
