import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LeaveAllocationFormService } from './leave-allocation-form.service';
import { LeaveAllocationService } from '../service/leave-allocation.service';
import { ILeaveAllocation } from '../leave-allocation.model';

import { LeaveAllocationUpdateComponent } from './leave-allocation-update.component';

describe('LeaveAllocation Management Update Component', () => {
  let comp: LeaveAllocationUpdateComponent;
  let fixture: ComponentFixture<LeaveAllocationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let leaveAllocationFormService: LeaveAllocationFormService;
  let leaveAllocationService: LeaveAllocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LeaveAllocationUpdateComponent],
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
      .overrideTemplate(LeaveAllocationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LeaveAllocationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    leaveAllocationFormService = TestBed.inject(LeaveAllocationFormService);
    leaveAllocationService = TestBed.inject(LeaveAllocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const leaveAllocation: ILeaveAllocation = { id: 456 };

      activatedRoute.data = of({ leaveAllocation });
      comp.ngOnInit();

      expect(comp.leaveAllocation).toEqual(leaveAllocation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeaveAllocation>>();
      const leaveAllocation = { id: 123 };
      jest.spyOn(leaveAllocationFormService, 'getLeaveAllocation').mockReturnValue(leaveAllocation);
      jest.spyOn(leaveAllocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaveAllocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaveAllocation }));
      saveSubject.complete();

      // THEN
      expect(leaveAllocationFormService.getLeaveAllocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(leaveAllocationService.update).toHaveBeenCalledWith(expect.objectContaining(leaveAllocation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeaveAllocation>>();
      const leaveAllocation = { id: 123 };
      jest.spyOn(leaveAllocationFormService, 'getLeaveAllocation').mockReturnValue({ id: null });
      jest.spyOn(leaveAllocationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaveAllocation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: leaveAllocation }));
      saveSubject.complete();

      // THEN
      expect(leaveAllocationFormService.getLeaveAllocation).toHaveBeenCalled();
      expect(leaveAllocationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILeaveAllocation>>();
      const leaveAllocation = { id: 123 };
      jest.spyOn(leaveAllocationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ leaveAllocation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(leaveAllocationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
