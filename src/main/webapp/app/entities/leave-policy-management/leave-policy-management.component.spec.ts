import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeavePolicyManagementComponent } from './leave-policy-management.component';

describe('LeavePolicyManagementComponent', () => {
  let component: LeavePolicyManagementComponent;
  let fixture: ComponentFixture<LeavePolicyManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeavePolicyManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LeavePolicyManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
