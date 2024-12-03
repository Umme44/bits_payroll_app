import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaveAllocationDetailComponent } from './leave-allocation-detail.component';

describe('LeaveAllocation Management Detail Component', () => {
  let comp: LeaveAllocationDetailComponent;
  let fixture: ComponentFixture<LeaveAllocationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaveAllocationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaveAllocation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaveAllocationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaveAllocationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaveAllocation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaveAllocation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
