// test/js/hsx.test.js
import { Button, ButtonViaReactElem, FragmentedButton, SeqButton, CallableButton, CallableButtonList, ShorthandTags, EmptyVectorChild } from '../../target/test/hsx-test';
import { render, screen, fireEvent } from '@testing-library/react';
import React from "react";

test('Button calls onClick when clicked', () => {
    const handleClick = jest.fn();

    render(<Button onClick={handleClick}>Click Me</Button>);

    // Simulate a click event
    fireEvent.click(screen.getByText(/Click Me/i));
    // Assert the click handler was called
    expect(handleClick).toHaveBeenCalledTimes(1);
});

test('ButtonViaReactElem calls onClick when clicked', () => {
    const handleClick = jest.fn();

    render(<ButtonViaReactElem onClick={handleClick}>Click Me</ButtonViaReactElem>);

    // Simulate a click event
    fireEvent.click(screen.getByText(/Click Me/i));
    // Assert the click handler was called
    expect(handleClick).toHaveBeenCalledTimes(1);
});

test('FragmentedButton calls onClick when clicked', () => {
    const handleClick = jest.fn();

    render(<FragmentedButton onClick={handleClick} buttonOneValue="ButtonOne" buttonTwoValue="ButtonTwo" />)

    // Simulate a click event
    fireEvent.click(screen.getByText(/ButtonOne/i));
    fireEvent.click(screen.getByText(/ButtonTwo/i));

    // Assert the click handler was called
    expect(handleClick).toHaveBeenCalledTimes(2);
});

test('SeqButton calls onClick when clicked', () => {
    const handleClick = jest.fn();

    render(<SeqButton onClick={handleClick} buttonOneValue="ButtonOne" buttonTwoValue="ButtonTwo" />)

    // Simulate a click event
    fireEvent.click(screen.getByText(/ButtonOne/i));
    fireEvent.click(screen.getByText(/ButtonTwo/i));

    // Assert the click handler was called
    expect(handleClick).toHaveBeenCalledTimes(2);
});

test('CallableButton calls onClick when rendered through a function call', () => {
    const handleClick = jest.fn();

    render(<CallableButton onClick={handleClick}>Click Me</CallableButton>);

    fireEvent.click(screen.getByText(/Click Me/i));
    expect(handleClick).toHaveBeenCalledTimes(1);
});

test('CallableButtonList keeps function-called components interactive', () => {
    const handleClick = jest.fn();

    render(<CallableButtonList onClick={handleClick} buttonOneValue="ButtonOne" buttonTwoValue="ButtonTwo" />)

    fireEvent.click(screen.getByText(/ButtonOne/i));
    fireEvent.click(screen.getByText(/ButtonTwo/i));
    expect(handleClick).toHaveBeenCalledTimes(2);
});

test('ShorthandTags renders div defaults and legacy id syntax', () => {
    const { container } = render(<ShorthandTags />);

    expect(container.querySelector('div.outer')).not.toBeNull();
    expect(container.querySelector('div#target').textContent).toBe('Target');
    expect(container.querySelector('div#legacy-id').textContent).toBe('Legacy');
    expect(container.querySelector('button#action.primary').textContent).toBe('Action');
    expect(container.querySelector('button#secondary-action.secondary').textContent).toBe('Secondary');
});

test('Empty vectors render as empty children', () => {
    render(<EmptyVectorChild />);

    expect(screen.getByText(/Rendered/i)).not.toBeNull();
});
